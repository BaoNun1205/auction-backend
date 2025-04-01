package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.notification.;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.notification.Notification;

public interface NotificationMapper {
    Notification toNotification(NotificationRequest notificationResponse);
    NotificationResponse toNotificationResponse(Notification notification);
}
