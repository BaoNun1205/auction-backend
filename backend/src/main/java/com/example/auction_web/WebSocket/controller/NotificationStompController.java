package com.example.auction_web.WebSocket.controller;

import com.example.auction_web.dto.request.notification.NotificationRequest;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@EnableScheduling
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NotificationStompController {
    NotificationService notificationService;
    SimpMessagingTemplate messagingTemplate;
    UserService userService;

    // Xử lý yêu cầu tạo thông báo từ client
    @MessageMapping("/rt-notification/send/{receiverId}")
    public void sendNotification(
            @DestinationVariable String receiverId,
            NotificationRequest notificationResquest) {
        try {
            Notification notification = Notification.builder()
                    .sender(userService.getUser(notificationResquest.getSenderId()))
                    .receiver(userService.getUser(notificationResquest.getReceiverId()))
                    .type((notificationResquest.getType()))
                    .title(notificationResquest.getTitle())
                    .content(notificationResquest.getContent())
                    .referenceId(notificationResquest.getReferenceId())
                    .isRead(false)
                    .build();

            NotificationResponse createdNotification = notificationService.createNotification(notification);

            ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                    .code(200)
                    .result(createdNotification)
                    .message("Notification sent successfully")
                    .build();

            messagingTemplate.convertAndSend(
                    "/rt-notification/user/" + receiverId,
                    response
            );
        } catch (RuntimeException e) {
            ApiResponse<NotificationResponse> errorResponse = ApiResponse.<NotificationResponse>builder()
                    .code(400)
                    .message("Failed to send notification: " + e.getMessage())
                    .build();

            messagingTemplate.convertAndSend(
                    "/rt-notification/user/" + receiverId,
                    errorResponse
            );
        }
    }
}
