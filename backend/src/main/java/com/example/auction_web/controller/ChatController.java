package com.example.auction_web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.dto.response.chat.MessageResponse;
import com.example.auction_web.entity.chat.Conversation;
import com.example.auction_web.entity.chat.Message;
import com.example.auction_web.service.chat.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/conversations")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ChatController {
    @Autowired
    private ChatService chatService;

    @GetMapping
    public ApiResponse<List<ConversationResponse>> getConversations(@RequestParam String userId) {
        return ApiResponse.<List<ConversationResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(chatService.getConversations(userId))
                .build();
    }

    @GetMapping("/messages/{conversationId}")
    public ApiResponse<List<MessageResponse>> getMessages(@PathVariable String conversationId) {
        return ApiResponse.<List<MessageResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(chatService.getMessages(conversationId))
                .build();
    }

    @PostMapping("/messages/{conversationId}")
    public ApiResponse<Message> sendMessage(
            @PathVariable String conversationId,
            @RequestBody Map<String, String> payload) {
        try {
            Message result = chatService.sendMessage(conversationId, payload);
            return ApiResponse.<Message>builder()
                    .code(HttpStatus.OK.value())
                    .result(result)
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<Message>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .result(null)
                    .build();
        }
    }
}
