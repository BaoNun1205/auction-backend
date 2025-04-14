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
    NotificationService notificationService;
    SimpMessagingTemplate messagingTemplate;
    AuctionSessionService auctionService;
    UserService userService;
    NotificationStompService notificationStompService;

    // Gửi thông báo tin nhắn đến một người nhận cụ thể
    @MessageMapping("/rt-auction/notification/new-message/user/{receiverId}")
    public void sendMessageNotification(@DestinationVariable String receiverId,
                                        NotificationRequest notificationRequest) {
        notificationRequest.setType(NotificationType.MESSAGE);
        notificationStompService.sendMessageNotification(receiverId, notificationRequest);
    }

    // Gửi thông báo có người đăng ký mới đến chủ phiên
    @MessageMapping("/rt-notification/new-register/{sessionId}")
    public void sendNewRegisterNotification(@DestinationVariable String sessionId, NotificationRequest notificationRequest) {
        try {
                // Lấy thông tin phiên đấu giá
                AuctionSessionResponse session = auctionService.getAuctionSessionById(sessionId);
                String ownerId = session.getUser().getUserId(); // Giả sử AuctionSessionResponse có phương thức getOwnerId()

                // Gửi thông báo đến chủ phiên đấu giá
                sendNotification(ownerId, notificationRequest, "/rt-notification/owner/");
        } catch (RuntimeException e) {
                ApiResponse<NotificationResponse> errorResponse = ApiResponse.<NotificationResponse>builder()
                        .code(400)
                        .message("Failed to send notification: " + e.getMessage())
                        .build();

                messagingTemplate.convertAndSend(
                        "/rt-notification/session/" + sessionId,
                        errorResponse
                );
        }
    }

    // Gửi thông báo có người đặt giá mới đến tất cả người trong phiên đấu giá
    @MessageMapping("/rt-notification/new-bid/{sessionId}")
    public void sendNewBidNotification(@DestinationVariable String sessionId, NotificationRequest notificationRequest) {
        sendNotification(sessionId, notificationRequest, "/rt-notification/session/");
    }

    // Hàm chung để gửi thông báo theo đường dẫn phù hợp
    private void sendNotification(String targetId, NotificationRequest notificationRequest, String topicPrefix) {
        try {
            Notification notification = Notification.builder()
                    .sender(userService.getUser(notificationRequest.getSenderId()))
                    .receiver(userService.getUser(notificationRequest.getReceiverId()))
                    .type(notificationRequest.getType())
                    .title(notificationRequest.getTitle())
                    .content(notificationRequest.getContent())
                    .referenceId(notificationRequest.getReferenceId())
                    .build();

            NotificationResponse createdNotification = notificationService.createNotification(notification);

            ApiResponse<NotificationResponse> response = ApiResponse.<NotificationResponse>builder()
                    .code(200)
                    .result(createdNotification)
                    .message("Notification sent successfully")
                    .build();

            messagingTemplate.convertAndSend(
                    topicPrefix + targetId,
                    response
            );
        } catch (RuntimeException e) {
            ApiResponse<NotificationResponse> errorResponse = ApiResponse.<NotificationResponse>builder()
                    .code(400)
                    .message("Failed to send notification: " + e.getMessage())
                    .build();

            messagingTemplate.convertAndSend(
                    topicPrefix + targetId,
                    errorResponse
            );
        }
    }
}
