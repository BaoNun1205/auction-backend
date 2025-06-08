package com.example.auction_web.WebSocket.service;

import java.util.List;

import com.example.auction_web.dto.response.auth.UserInfoBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class NotificationStompServiceImpl implements NotificationStompService {
    NotificationService notificationService;
    UserService userService;
    SimpMessagingTemplate messagingTemplate;
    AuctionSessionService auctionSessionService;

    @Transactional
    @Override
    public void sendUserNotification(String receiverId, NotificationRequest notificationRequest) {
        log.info("Sending notification for receiverId: {}", receiverId);
        log.info("Notification details: senderId={}, receiverId={}, type={}, title={}, content={}, referenceId={}",
                 notificationRequest.getSenderId(),
                 notificationRequest.getReceiverId(),
                 notificationRequest.getType(),
                 notificationRequest.getTitle(),
                 notificationRequest.getContent(),
                 notificationRequest.getReferenceId());
        sendNotification(receiverId, notificationRequest, "/rt-notification/user/");
    }

    @Override
    public void sendNewBidNotification(String sessionId, NotificationRequest notificationRequest) {
        try {
            // Lấy tất cả user đã từng đặt giá trong phiên
            List<User> receivers = auctionSessionService.getUsersBiddingInSession(sessionId);
    
            User sender = userService.getUser(notificationRequest.getSenderId());

            UserInfoBase sessionOwner = auctionSessionService.getAuctionSessionById(sessionId).getAsset().getVendor();

            // Lọc ra những user khác sender
            receivers.stream()
                .filter(receiver -> !receiver.getUserId().equals(sender.getUserId()))
                .forEach(receiver -> {
                    try {
                        NotificationRequest clonedRequest = NotificationRequest.builder()
                            .senderId(notificationRequest.getSenderId())
                            .receiverId(receiver.getUserId())
                            .title(notificationRequest.getTitle())
                            .content(notificationRequest.getContent())
                            .referenceId(notificationRequest.getReferenceId())
                            .type(notificationRequest.getType())
                            .build();
            
                        sendUserNotification(receiver.getUserId(), clonedRequest);
                    } catch (Exception e) {
                        log.warn("Failed to save notification for user: {}", receiver.getUserId(), e);
                    }
                });

            try {
                NotificationRequest clonedOwnerRequest = NotificationRequest.builder()
                            .senderId(notificationRequest.getSenderId())
                            .receiverId(sessionOwner.getUserId())
                            .title("Phiên đấu giá của bạn đã có người đặt giá mới!")
                            .content(notificationRequest.getContent())
                            .referenceId(notificationRequest.getReferenceId())
                            .type(notificationRequest.getType())
                            .build();
            
                sendUserNotification(sessionOwner.getUserId(), clonedOwnerRequest);
            } catch (Exception e) {
                log.error("Error sending notification to auction session topic", e);
            }

        } catch (Exception e) {
            log.error("Error broadcasting bid notification", e);
        }
    }

    private void sendNotification(String targetId, NotificationRequest notificationRequest, String topicPrefix) {
        try {
            log.info("Preparing notification for targetId: {}, topic: {}", targetId, topicPrefix + targetId);
            Notification.NotificationBuilder builder = Notification.builder()
                .sender(userService.getUser(notificationRequest.getSenderId()))
                .type(notificationRequest.getType())
                .title(notificationRequest.getTitle())
                .content(notificationRequest.getContent())
                .referenceId(notificationRequest.getReferenceId());

            // Nếu receiverId không null thì map receiver
            if (notificationRequest.getReceiverId() != null) {
                builder.receiver(userService.getUser(notificationRequest.getReceiverId()));
            }

            Notification notification = builder.build();

            log.info("Creating notification in database...");
            NotificationResponse response = notificationService.createNotification(notification);
            log.info("Notification created: {}", response);

            log.info("Sending notification to WebSocket topic: {}, response: {}", topicPrefix + targetId, response);
            messagingTemplate.convertAndSend(
                    topicPrefix + targetId,
                    response
            );
            log.info("Notification sent successfully to: {}", topicPrefix + targetId);

        } catch (RuntimeException e) {
            log.info("Error response sent to: {}", topicPrefix + targetId);
        }
    }
}