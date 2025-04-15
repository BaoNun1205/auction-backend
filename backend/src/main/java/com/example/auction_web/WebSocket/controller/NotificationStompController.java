package com.example.auction_web.WebSocket.controller;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

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
