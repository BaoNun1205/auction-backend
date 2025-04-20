package com.example.auction_web.WebSocket.controller;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Controller
@EnableScheduling
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Slf4j
public class NotificationStompController {
    NotificationStompService notificationStompService;

    // Gửi thông báo tin nhắn đến một người nhận cụ thể
    @MessageMapping("/rt-auction/notification/new-message/user/{receiverId}")
    public void sendMessageNotification(@DestinationVariable String receiverId,
                                        NotificationRequest notificationRequest) {
        notificationStompService.sendUserNotification(receiverId, notificationRequest);
    }
}
