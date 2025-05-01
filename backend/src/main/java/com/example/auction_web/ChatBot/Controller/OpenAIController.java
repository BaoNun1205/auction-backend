package com.example.auction_web.ChatBot.Controller;

import com.example.auction_web.ChatBot.Dto.BotConversationResponse;
import com.example.auction_web.ChatBot.Dto.BotMessageResponse;
import com.example.auction_web.ChatBot.Dto.ChatRequest;
import com.example.auction_web.ChatBot.Dto.ConversationRequest;
import com.example.auction_web.ChatBot.Service.ChatBotService;
import com.example.auction_web.ChatBot.Service.OpenAIService;
import com.example.auction_web.dto.response.ApiResponse;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class OpenAIController {

    OpenAIService openAIService;
    ChatBotService chatBotService;

    @PostMapping("/test-tool-call")
    public ResponseEntity<?> testChatWithToolCall(@RequestBody ChatRequest request) {
        try {
            var response = openAIService.chatWithToolCalls(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    
    @PostMapping("/conversations")
    public ApiResponse<BotConversationResponse> createConversation(@RequestBody ConversationRequest request) {
        try {
            BotConversationResponse response = chatBotService.createConversation(request.getUserId());
            return ApiResponse.<BotConversationResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(response)
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<BotConversationResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message(e.getMessage())
                    .build();
        }
    }    

    @GetMapping("/conversations")
    public ResponseEntity<List<BotConversationResponse>> getConversations(@RequestParam String userId) {
        List<BotConversationResponse> conversations = chatBotService.getConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/messages/{conversationId}")
    public ResponseEntity<List<BotMessageResponse>> getMessages(@PathVariable String conversationId) {
        List<BotMessageResponse> messages = chatBotService.getMessages(conversationId);
        return ResponseEntity.ok(messages);
    }
}
