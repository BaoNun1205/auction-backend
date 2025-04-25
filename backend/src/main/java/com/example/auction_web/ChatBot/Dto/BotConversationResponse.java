package com.example.auction_web.ChatBot.Dto;

import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotConversationResponse {
    String id;
    String topic;
    UserResponse user;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
