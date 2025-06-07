package com.example.auction_web.ChatBot.Service;

import com.example.auction_web.ChatBot.Dto.ChatRequest;
import com.example.auction_web.ChatBot.Dto.FilterSessionDto;
import com.example.auction_web.ChatBot.Dto.MessageCreateRequestDto;
import com.example.auction_web.ChatBot.Enum.Role;
import com.example.auction_web.Payment.Dto.VNPayDTO;
import com.example.auction_web.Payment.Dto.VNPayRequestDTO;
import com.example.auction_web.Payment.Service.VNPayService;
import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.service.AuctionHistoryService;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.BalanceUserService;
import com.example.auction_web.service.DepositService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OpenAIService {
    @Value("${openai.azure.api-key}")
    private String azureApiKey;

    @Value("${openai.azure.endpoint}")
    private String azureEndpoint;

    @Value("${openai.azure.deployment-name}")
    private String deploymentName;

    @Autowired
    private final AuctionSessionService auctionSessionService;

    @Autowired
    private final AuctionHistoryService auctionHistoryService;

    @Autowired
    private final BalanceUserService balanceUserService;

    @Autowired
    private final DepositService depositService;

    @Autowired
    private final VNPayService vnPayService;

    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private final ChatBotService chatBotService;

    public OpenAIService(RestTemplate restTemplate, AuctionSessionService auctionSessionService, ChatBotService chatBotService, AuctionHistoryService auctionHistoryService, BalanceUserService balanceUserService, VNPayService vnPayService, DepositService depositService) {
        this.restTemplate = restTemplate;
        this.auctionSessionService = auctionSessionService;
        this.chatBotService = chatBotService;
        this.auctionHistoryService = auctionHistoryService;
        this.balanceUserService = balanceUserService;
        this.vnPayService = vnPayService;
        this.depositService = depositService;
    }

    public String chatWithToolCalls(ChatRequest request) throws Exception {
        var MessageList = chatBotService.getMessages(request.getConversationId());
        List<Map<String, Object>> messages = new ArrayList<>();

        // Add system prompt
        messages.add(Map.of(
                "role", Role.system,
                "content", systemPrompt()
        ));

        // Add message history
        if (MessageList != null) {
            for (var message : MessageList) {
                messages.add(Map.of(
                        "role", message.getRole(),
                        "content", message.getContent()
                ));
            }
        }

        // Add user message
        messages.add(Map.of(
                "role", Role.user,
                "content", request.getContent()
        ));

        List<Map<String, Object>> functions = getDefinedFunctions();

        boolean hasToolCalls = true;
        String responseBody = null;

        JsonNode responseJson = null;
        while (hasToolCalls) {
            responseBody = callOpenAI(messages, functions);
            responseJson = mapper.readTree(responseBody);

            JsonNode toolCall = responseJson.at("/choices/0/message/function_call");

            if (toolCall != null && !toolCall.isMissingNode() && toolCall.isObject()) {
                String functionName = toolCall.get("name").asText();
                JsonNode argumentsNode = toolCall.get("arguments");

                String toolResponse = processToolCall(functionName, argumentsNode);

                // Add assistant message with function_call
                Map<String, Object> assistantMsg = new HashMap<>();
                assistantMsg.put("role", "assistant");
                assistantMsg.put("content", null);
                assistantMsg.put("function_call", Map.of(
                        "name", functionName,
                        "arguments", argumentsNode
                ));
                messages.add(assistantMsg);

                // Add tool message with function name and response
                Map<String, Object> toolMsg = new HashMap<>();
                toolMsg.put("role", "function");
                toolMsg.put("name", functionName);
                toolMsg.put("content", toolResponse);
                messages.add(toolMsg);
            } else {
                hasToolCalls = false;
            }
        }

        JsonNode finalMessageNode = responseJson.at("/choices/0/message/content");
        if (!finalMessageNode.isMissingNode()) {
            addMessage(request.getConversationId(), Role.user, request.getContent());
            addMessage(request.getConversationId(), Role.assistant, finalMessageNode.asText());
            return finalMessageNode.asText();
        } else {
            return "[Không có nội dung trả về từ assistant]";
        }
    }

    public String callOpenAI(List<Map<String, Object>> messages, List<Map<String, Object>> functions) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("messages", messages);
        requestBody.put("functions", functions);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", azureApiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Azure URL format:
        String url = String.format(
                "%s/openai/deployments/%s/chat/completions?api-version=2024-02-15-preview",
                azureEndpoint, deploymentName
        );

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, request, String.class);
        return response.getBody();
    }


    private String processToolCall(String functionName, JsonNode argumentsNode) throws Exception {

        if (argumentsNode.isTextual()) {
            argumentsNode = mapper.readTree(argumentsNode.asText());
        }

        switch (functionName) {
            case "FilterAuctionSession":
                FilterSessionDto args1 = mapper.treeToValue(argumentsNode, FilterSessionDto.class);
                return auctionSessionService.filterAuctionSession(
                        args1.getStatus(),
                        args1.getTypeId(),
                        args1.getUserId(),
                        args1.getFromDate(),
                        args1.getToDate(),
                        args1.getMinPrice(),
                        args1.getMaxPrice(),
                        args1.getKeyword(),
                        args1.getIsInCrease(),
                        args1.getPage(),
                        args1.getSize()
                ).toString();

            case "PlaceBidSession":
                try {
                    AuctionHistoryCreateRequest placeBidSession = mapper.treeToValue(argumentsNode, AuctionHistoryCreateRequest.class);
                    return auctionHistoryService.createAuctionHistory(placeBidSession).toString();
                } catch (AppException ex) {
                    return ex.getMessage();
                }

            case "GetMyBalanceUser":
                return balanceUserService.getMyCoinUser().toString();

            case "PaymentVnPay":
                VNPayRequestDTO vnpayDTO = mapper.treeToValue(argumentsNode, VNPayRequestDTO.class);
                vnpayDTO.setBankCode("NCB");
                var response = vnPayService.createVnPayPayment(vnpayDTO, "");
                return response.paymentUrl.toString();

            case "depositSessionTool":
                try {
                    DepositCreateRequest deposit = mapper.treeToValue(argumentsNode, DepositCreateRequest.class);
                    return depositService.createDeposit(deposit).toString();
                } catch (AppException ex) {
                    return ex.getMessage();
                }

            default:
                throw new IllegalArgumentException("Function not supported: " + functionName);
        }
    }

    private void addMessage(String conversationId, Role role, String content) {
        chatBotService.createMessage(new MessageCreateRequestDto(conversationId, role, content));
    }

    private String systemPrompt() {
        var system_time = LocalDateTime.now();
        return """
            You are a smart assistant capable of using various tools such as API calls and data lookup to answer user questions. Analyze the context to choose the appropriate tool and provide accurate, clear, and polite responses. If uncertain, clearly communicate that.
            
            <h3>⚙️ General Rules</h3>
            <ul>
              <li>Always respond in the same language the user is using.</li>
              <li>Respond using simple HTML format suitable for web display:
                <ul>
                  <li>Bold text: use <strong>.</li>
                  <li>Lists: use <ul><li>.</li>
                  <li>Headings: use <h3>.</li>
                  <li>Images: use &lt;img&gt; with style="max-width:100%; height:auto;" or class="auction-image".</li>
                </ul>
              </li>
              <li>Do not use markdown or triple backticks (```).</li>
              <li>If a tool is needed, map parameters correctly and call the appropriate function.</li>
              <li>If an API call fails, report the error clearly to the user.</li>
            </ul>
            
            <h3>📦 Specific Instructions</h3>
            <ul>
              <li><strong>Auction Session List:</strong> If the user does not provide filters, use default values. If only some filters are provided, leave the others null.</li>
                <li><strong>Auction Session Details:</strong> Always display all the following fields, including the <strong>auctionSessionId (UUID)</strong>:
                  <ul>
                    <li><strong>ID:</strong> [auctionSessionId] (UUID, always include this)</li>
                    <li><strong>Session Name:</strong> [Auction Name]</li>
                    <li><strong>Description:</strong> [Description]</li>
                    <li><strong>Type:</strong> [Auction Type]</li>
                    <li><strong>Start Time:</strong> yyyy-MM-dd HH:mm:ss</li>
                    <li><strong>End Time:</strong> yyyy-MM-dd HH:mm:ss</li>
                    <li><strong>Starting Price:</strong> [Starting Price] VND</li>
                    <li><strong>Bid Increment:</strong> [Bid Increment] VND</li>
                    <li><strong>Current Highest Bid:</strong> [Current Highest Bid] VND</li>
                    <li><strong>Status:</strong> [Status]</li>
                    <li><strong>Image:</strong> displayed using &lt;img&gt;</li>
                  </ul>
                </li>
              <li><strong>Placing a Bid:</strong>
                <ul>
                  <li>If auctionSessionId is missing, call FilterAuctionSession with status = ONGOING.</li>
                  <li>Ask the user to select a suitable session name.</li>
                  <li>Look up the corresponding session ID and call placeBidSessionTool with auctionSessionId and bidAmount.</li>
                </ul>
              </li>
              <li><strong>Account Top-up:</strong>
                <ul>
                  <li>If amount is missing, ask the user to enter it.</li>
                  <li>Call PaymentVnPay with the amount.</li>
                  <li>If paymentUrl is returned, show the link “Click here to proceed with the payment.”</li>
                  <li>If not returned or error occurs, notify the user to retry or contact support.</li>
                </ul>
              </li>
              <li><strong>Deposit Submission:</strong>
                <ul>
                  <li>Check the last 6–8 messages for an attempt to bid that failed due to missing deposit.</li>
                  <li>If session name is found, look up ID using FilterAuctionSession and call depositSessionTool.</li>
                  <li>If no context is found, call FilterAuctionSession (ONGOING), ask for session name, find ID, and then proceed with deposit.</li>
                </ul>
              </li>
              <li><strong>View Account Info:</strong> Call getMyBalanceUser.</li>
            </ul>
            
            <h3>📌 Notes</h3>
            <ul>
              <li>Never assume sessionId based on the name. Always look it up using FilterAuctionSession.</li>
              <li>Only call tools when sufficient and valid information is available and the logic flow makes sense.</li>
            </ul>
            
            <p><em>Current datetime:</em> """ + system_time + "</p>";
    }

    private List<Map<String, Object>> getDefinedFunctions() {
        List<Map<String, Object>> functions = new ArrayList<>();

        // 1. FilterSession
        Map<String, Object> filterAuctionSessionTool = Map.of(
                "name", "FilterAuctionSession",
                "description", "Filter auction sessions based on various criteria such as status, type, user, time, price, and keyword.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", getFilterAuctionSessionProperties(),
                        "required", List.of()
                )
        );

        Map<String, Object> placeBidSessionTool = Map.of(
                "name", "PlaceBidSession",
                "description", "Place a bid on an auction session.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "auctionSessionId", Map.of(
                                        "type", "string",
                                        "description", "ID of the auction session."
                                ),
                                "bidPrice", Map.of(
                                        "type", "number",
                                        "format", "decimal",
                                        "description", "The bid price."
                                )
                        ),
                        "required", List.of("auctionSessionId", "bidPrice")
                )
        );

        Map<String, Object> getMyBalanceUser = Map.of(
                "name", "GetMyBalanceUser",
                "description", "Get the current user's balance.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of()
                )
        );

        Map<String, Object> PaymentVnPay = Map.of(
                "name", "PaymentVnPay",
                "description", "",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "amount", Map.of(
                                        "type", "string",
                                        "description", "The amount of the payment via VN."

                                )
                        ),
                        "required", List.of("amount")
                )
        );

        Map<String, Object> depositSessionTool = Map.of(
                "name", "depositSessionTool",
                "description", "Make a deposit to confirm participation in a selected auction session.",
                "parameters", Map.of(
                        "type", "object",
                        "properties", Map.of(
                                "auctionSessionId", Map.of(
                                        "type", "string",
                                        "description", "The ID of the auction session the user wants to join."
                                ),
                                "userConfirmation", Map.of(
                                        "type", "boolean",
                                        "description", "Whether the user has confirmed the information to create the request. Values: \"true\" or \"false\". If not confirmed, the value is always \"false\"."
                                )
                        ),
                        "required", List.of("auctionSessionId", "userConfirmation")
                )
        );

        functions.add(filterAuctionSessionTool);
        functions.add(placeBidSessionTool);
        functions.add(getMyBalanceUser);
        functions.add(PaymentVnPay);
        functions.add(depositSessionTool);

        return functions;
    }

    private Map<String, Object> getFilterAuctionSessionProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put("status", Map.of(
                "type", "string",
                "description", "Status of the auction session. Optional. Leave empty to ignore. Possible values: UPCOMING (about to start), ONGOING (currently ongoing), AUCTION_SUCCESS (successfully auctioned), AUCTION_FAILED (auction failed).",
                "enum", List.of("UPCOMING", "ONGOING", "AUCTION_SUCCESS", "AUCTION_FAILED")
        ));
        properties.put("typeId", Map.of(
                "type", "string",
                "description", "ID of the auction type. Optional. Leave empty to ignore."
        ));
        properties.put("userId", Map.of(
                "type", "string",
                "description", "ID of the user. Optional. Leave empty to ignore."
        ));
        properties.put("fromDate", Map.of(
                "type", "string",
                "format", "date-time",
                "description", "Start of the date range for auction session. Optional."
        ));
        properties.put("toDate", Map.of(
                "type", "string",
                "format", "date-time",
                "description", "End of the date range for auction session. Optional."
        ));
        properties.put("minPrice", Map.of(
                "type", "number",
                "format", "decimal",
                "description", "Minimum price filter. Optional."
        ));
        properties.put("maxPrice", Map.of(
                "type", "number",
                "format", "decimal",
                "description", "Maximum price filter. Optional."
        ));
        properties.put("keyword", Map.of(
                "type", "string",
                "description", "Keyword to search by asset name or description. Optional."
        ));
        properties.put("isInCrease", Map.of(
                "type", "boolean",
                "description", "Filter auction sessions where prices are increasing. Optional."
        ));
        properties.put("page", Map.of(
                "type", "integer",
                "description", "Page index (zero-based). Optional. Defaults to 0 if not provided.",
                "default", 0
        ));
        properties.put("size", Map.of(
                "type", "integer",
                "description", "Number of records per page. Optional. Defaults to 10 if not provided.",
                "default", 10
        ));

        return properties;
    }

}
