package com.example.auction_web.WebSocket.service;

import com.example.auction_web.dto.request.notification.NotificationRequest;

public interface NotificationStompService {
    void sendUserNotification(String receiverId, NotificationRequest notificationRequest);
    void sendNewBidNotification(String sessionId, NotificationRequest notificationRequest);
}
