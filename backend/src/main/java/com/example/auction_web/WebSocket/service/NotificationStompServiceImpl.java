package com.example.auction_web.WebSocket.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class NotificationStompServiceImpl implements NotificationStompService {
    NotificationService notificationService;
    UserService userService;
    SimpMessagingTemplate messagingTemplate;

    // Implement the methods to send notifications to the WebSocket clients
    @Override
    public void sendMessageNotification(String receiverId, NotificationRequest notificationRequest) {
        log.info("Calling sendMessageNotification with receiverId: {}, request: {}", receiverId, notificationRequest);
        sendNotification(receiverId, notificationRequest, "/rt-notification/new-message/user/");
    }

    @Override
    public void sendNewRegisterNotification(String ownerId, NotificationRequest notificationRequest) {
        log.info("Calling sendNewRegisterNotification with ownerId: {}, request: {}", ownerId, notificationRequest);
        sendNotification(ownerId, notificationRequest, "/rt-notification/new-register/owner/");
    }

    @Override
    public void sendNewBidNotification(String sessionId, NotificationRequest notificationRequest) {
        log.info("Calling sendNewBidNotification with sessionId: {}, request: {}", sessionId, notificationRequest);
        sendNotification(sessionId, notificationRequest, "/rt-notification/new-bid/session/");
    }

    private void sendNotification(String targetId, NotificationRequest notificationRequest, String topicPrefix) {
        try {
            log.info("Preparing notification for targetId: {}, topic: {}", targetId, topicPrefix + targetId);
            Notification notification = Notification.builder()
                    .sender(userService.getUser(notificationRequest.getSenderId()))
                    .receiver(userService.getUser(notificationRequest.getReceiverId()))
                    .type(notificationRequest.getType())
                    .title(notificationRequest.getTitle())
                    .content(notificationRequest.getContent())
                    .referenceId(notificationRequest.getReferenceId())
                    .isRead(false)
                    .build();

            log.info("Creating notification in database...");
            NotificationResponse createdNotification = notificationService.createNotification(notification);
            log.info("Notification created: {}", createdNotification);

            ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                    .code(200)
                    .result(createdNotification)
                    .message("Notification sent successfully")
                    .build();

            log.info("Sending notification to WebSocket topic: {}, response: {}", topicPrefix + targetId, response);
            messagingTemplate.convertAndSend(
                    topicPrefix + targetId,
                    response
            );
            log.info("Notification sent successfully to: {}", topicPrefix + targetId);

        } catch (RuntimeException e) {
            log.error("Error while sending notification: {}", e.getMessage(), e);
            ApiResponse<NotificationResponse> errorResponse = ApiResponse.<NotificationResponse>builder()
                    .code(400)
                    .message("Failed to send notification: " + e.getMessage())
                    .build();

            log.info("Sending error response to WebSocket topic: {}, errorResponse: {}", topicPrefix + targetId, errorResponse);
            messagingTemplate.convertAndSend(
                    topicPrefix + targetId,
                    errorResponse
            );
            log.info("Error response sent to: {}", topicPrefix + targetId);
        }
    }
}