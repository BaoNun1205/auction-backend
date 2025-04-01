package com.example.auction_web.dto.response.notification;

import java.time.LocalDateTime;
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
public class NotificationResponse {
    String id;
    String sender;
    String receiver;
    NotificationType type;
    String title;
    String content;
    String referenceId;
    boolean isRead;
    LocalDateTime createdAt;
}
