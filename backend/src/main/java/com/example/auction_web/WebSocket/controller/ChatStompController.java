package com.example.auction_web.WebSocket.controller;

import java.util.Map;

import com.example.auction_web.dto.response.chat.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.entity.chat.Message;
import com.example.auction_web.service.chat.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@EnableScheduling
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ChatStompController {
    @Autowired
    private ChatService chatService;

    @MessageMapping("/rt-auction/conversations/{conversationId}")
    @SendTo("/rt-chat/conversations/{conversationId}")
    public ApiResponse<MessageResponse> sendMessage(
            @DestinationVariable String conversationId,
            @Payload Map<String, String> payload) {
        MessageResponse savedMessage = chatService.sendMessage(conversationId, payload);
        return ApiResponse.<MessageResponse>builder()
                .code(200)
                .result(savedMessage)
                .build();
    }
}
