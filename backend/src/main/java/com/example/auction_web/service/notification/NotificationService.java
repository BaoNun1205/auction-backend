package com.example.auction_web.service.notification;

import java.util.List;

import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.notification.Notification;

public interface NotificationService {
    public NotificationResponse createNotification(Notification notification);
    public List<NotificationResponse> getNotificationByReceiver(String receiverId);
    public void markAsRead(String notificationId);
    public long countUnreadNotifications(String receiverId);
}
