package com.example.auction_web.dto.response.notification;

import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;
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
    UserResponse sender;
    UserResponse receiver;
    NotificationType type;
    String title;
    String content;
    String referenceId;
    boolean read;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
