package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.notification.NotificationResponse;
import com.example.auction_web.entity.notification.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "sender", ignore = true)
    @Mapping(target = "receiver", ignore = true)
    Notification toNotification(NotificationRequest notificationResponse);
    NotificationResponse toNotificationResponse(Notification notification);
}
