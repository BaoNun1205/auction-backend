package com.example.auction_web.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.auction_web.dto.request.chat.ConversationRequest;
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

    @PostMapping
    public ApiResponse<ConversationResponse> createConversation(@RequestBody ConversationRequest request) {
        try {
            ConversationResponse result = chatService.createConversation(request);
            return ApiResponse.<ConversationResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(result)
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<ConversationResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .result(null)
                    .build();
        }
    }

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
    public ApiResponse<MessageResponse> sendMessage(
            @PathVariable String conversationId,
            @RequestBody Map<String, String> payload) {
        try {
            MessageResponse result = chatService.sendMessage(conversationId, payload);
            return ApiResponse.<MessageResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(result)
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<MessageResponse>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .result(null)
                    .build();
        }
    }

    @PutMapping("/unread/{conversationId}")
    public ApiResponse<Void> updateUnread(
            @PathVariable String conversationId,
            @RequestParam int unreadCount) {
        try {
            chatService.updateUnread(conversationId, unreadCount);
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.OK.value())
                    .message("Unread count updated successfully")
                    .build();
        } catch (RuntimeException e) {
            return ApiResponse.<Void>builder()
                    .code(HttpStatus.NOT_FOUND.value())
                    .message("Conversation not found")
                    .build();
        }
    }
}
