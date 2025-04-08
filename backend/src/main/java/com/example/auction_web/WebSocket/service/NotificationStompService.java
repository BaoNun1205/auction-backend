package com.example.auction_web.WebSocket.service;

import com.example.auction_web.dto.request.notification.NotificationRequest;

public interface NotificationStompService {
    void sendMessageNotification(String receiverId, NotificationRequest notificationRequest);
    void sendNewRegisterNotification(String ownerId, NotificationRequest notificationRequest);
    void sendNewBidNotification(String sessionId, NotificationRequest notificationRequest);
}
