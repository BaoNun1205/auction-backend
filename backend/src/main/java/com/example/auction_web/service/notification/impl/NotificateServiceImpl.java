package com.example.auction_web.service.notification.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.notification.Notification;
import com.example.auction_web.mapper.NotificationMapper;
import com.example.auction_web.repository.notification.NotificateRepository;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.service.notification.NotificationService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class NotificateServiceImpl implements NotificationService{
    NotificateRepository notificateRepository;
    NotificationMapper notificationMapper;
    UserService userService;

    public NotificationResponse createNotification(Notification notification) {
        try {
            return notificationMapper.toNotificationResponse(notificateRepository.save(notification));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create notification", e);
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
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificateRepository.save(notification);
    }

    public long countUnreadNotifications(String receiverId) {
        User receiver = userService.getUser(receiverId);
        return notificateRepository.countByReceiverAndIsReadFalseAndDelFlagFalse(receiver);
    }
}
