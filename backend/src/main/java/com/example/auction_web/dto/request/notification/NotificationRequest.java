package com.example.auction_web.dto.request.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import com.example.auction_web.enums.NotificationType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class NotificationRequest {
    String senderId;
    String receiverId;
    NotificationType type;
    String title;
    String content;
    String referenceId;
}
