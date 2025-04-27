package com.example.auction_web.ChatBot.Service;

import com.example.auction_web.ChatBot.Dto.ChatRequest;
import com.example.auction_web.ChatBot.Dto.FilterSessionDto;
import com.example.auction_web.ChatBot.Dto.MessageCreateRequestDto;
import com.example.auction_web.ChatBot.Enum.Role;
import com.example.auction_web.service.AuctionSessionService;
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

    private final RestTemplate restTemplate;
    @Autowired
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private final ChatBotService chatBotService;

    public OpenAIService(RestTemplate restTemplate, AuctionSessionService auctionSessionService, ChatBotService chatBotService) {
        this.restTemplate = restTemplate;
        this.auctionSessionService = auctionSessionService;
        this.chatBotService = chatBotService;
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

            default:
                throw new IllegalArgumentException("Function not supported: " + functionName);
        }
    }

    private void addMessage(String conversationId, Role role, String content) {
        chatBotService.createMessage(new MessageCreateRequestDto(conversationId, role, content));
    }

    private String systemPrompt() {
        var system_time = LocalDateTime.now();
        return "You are an intelligent assistant capable of answering user questions by utilizing multiple tools, including API calls and knowledge base lookups. You can select the most suitable function or tool to respond to the user based on the context of the question. Your role is to provide smart, context-aware answers, and optionally trigger tools when needed. You are polite, concise, and avoid hallucinations. If you're unsure, you will mention it explicitly.\n" +

                "\n" +
                "<general_guidelines>\n" +
                "- Always respond in the language the user is using.\n" +
                "- Always return responses in structured HTML format with basic styling (such as bold text, bullet points, and section headers) suitable for web display. Use clear and minimalistic HTML.\n " +
                "- Ensure that lists are <ul><li> formatted, important text is <strong> highlighted, and sections have <h3> titles.\n" +
                "- - Ensure images use <img> tags with responsive inline CSS (e.g., style=\"max-width:100%; height:auto;\") or add a class \"auction-image\" for frontend styling.\n" +
                "- Analyze the user's intent and decide whether to call an API, search a knowledge base, or use internal knowledge.\n" +
                "- If an API tool is available, map user-provided values to the API's parameters and call the appropriate method.\n" +
                "- In case the API fails, tell the user to try again next time.\n" +
                "- If the query is related to documented knowledge, use the knowledge base tools.\n" +
                "- If tools are not sufficient, rely on your internal model knowledge to provide a helpful answer.\n" +
                "- Respond in a clear and helpful manner, suitable for business communication.\n" +
                "</general_guidelines>\n" +

                "\n" +
                "<guidelines>\n" +
                "- Always respond in the same language as the user.\n" +
                "- When the user asks for a list of auction sessions without specifying filters, use default values for unspecified parameters. If some filters are specified, set the unspecified ones to null.\n" +
                "- When displaying auction session information, you must always include the following fields: " +
                "       Auction Name, Description, Auction Type, Start Time, End Time, Starting Price, Bid Increment, Current Highest Bid, Status, and Image.\n" +
                "- Translate the field labels (such as 'Auction Name', 'Start Time', etc.) into the user's language.\n" +
                "  🔹 **[Label: Auction Name]**: [Auction Name]\n" +
                "  - **[Label: Description]**: [Auction Description]\n" +
                "  - **[Label: Auction Type]**: [Auction Type]\n" +
                "  - **[Label: Start Time]**: [Start Time in yyyy-MM-dd HH:mm:ss format]\n" +
                "  - **[Label: End Time]**: [End Time in yyyy-MM-dd HH:mm:ss format]\n" +
                "  - **[Label: Starting Price]**: [Starting Price] VND\n" +
                "  - **[Label: Bid Increment]**: [Bid Increment] VND\n" +
                "  - **[Label: Current Highest Bid]**: [Current Highest Bid] VND\n" +
                "  - **[Label: Status]**: [Auction Status]\n" +
                "  - [Label: Image]: Display the image using an HTML <img> tag with the provided image URL. Add responsive styling (style=\"max-width:100%; height:auto;\") or apply the CSS class \"auction-image\".\n " +
                "- Ensure correct formatting and consistent translation of labels to match the user's language.\n" +
                "</guidelines>\n" +

                "\nCurrent datetime: " + system_time + "\n";
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

        // Add all
        functions.add(filterAuctionSessionTool);

        return functions;
    }

    private Map<String, Object> getFilterAuctionSessionProperties() {
        Map<String, Object> properties = new HashMap<>();

        properties.put("status", Map.of(
                "type", "string",
                "description", "Status of the auction session. Optional. Leave empty to ignore."
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
