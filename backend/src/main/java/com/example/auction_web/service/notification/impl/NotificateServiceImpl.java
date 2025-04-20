package com.example.auction_web.service.notification.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.NotificationMapper;
import com.example.auction_web.repository.notification.NotificateRepository;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class NotificateServiceImpl implements NotificationService{
    NotificateRepository notificateRepository;
    NotificationMapper notificationMapper;
    UserService userService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public NotificationResponse createNotification(Notification notification) {
        try {
            log.info("Saving notification to DB: {}", notification.getContent());

            // Nếu là thông báo tin nhắn
            if (notification.getType() == NotificationType.MESSAGE) {
                Optional<Notification> existing = notificateRepository
                        .findFirstBySenderAndReceiverAndTypeAndReferenceId(
                                notification.getSender(),
                                notification.getReceiver(),
                                NotificationType.MESSAGE,
                                notification.getReferenceId()
                        );

                if (existing.isPresent()) {
                    Notification oldNotification = existing.get();
                    oldNotification.setCreatedAt(LocalDateTime.now());
                    oldNotification.setRead(false);
                    oldNotification.setContent(notification.getContent());
                    
                    Notification updated = notificateRepository.save(oldNotification);
                    log.info("Updated existing message notification ID: {}", updated.getId());
                    return notificationMapper.toNotificationResponse(updated);
                }
            }

            // Nếu không có thì tạo mới
            Notification saved = notificateRepository.save(notification);
            log.info("Saved new notification ID: {}", saved.getId());
            return notificationMapper.toNotificationResponse(saved);

        } catch (Exception e) {
            log.error("Error creating notification", e);
            throw new AppException(ErrorCode.CREATE_NOTIFICATION_FAILED);
        }
    }    

    public List<NotificationResponse> getNotificationByReceiver(String receiverId) {
        User receiver = userService.getUser(receiverId);
        List<Notification> notifications = notificateRepository.findByReceiverAndDelFlagFalseOrderByCreatedAtDesc(receiver);
        return notifications.stream()
                .map(notificationMapper::toNotificationResponse)
                .toList();
    }

    public void markAsRead(String notificationId) {
        Notification notification = notificateRepository.findById(notificationId)
                .orElseThrow(() -> new AppException(ErrorCode.NOTIFICATION_NOT_EXISTED));
        notification.setRead(true);
        notificateRepository.save(notification);
    }

    public long countUnreadNotifications(String receiverId) {
        User receiver = userService.getUser(receiverId);
        return notificateRepository.countByReceiverAndIsReadFalseAndDelFlagFalse(receiver);
    }
}
