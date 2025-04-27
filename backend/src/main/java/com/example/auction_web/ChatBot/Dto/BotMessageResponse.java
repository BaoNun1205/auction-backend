package com.example.auction_web.ChatBot.Dto;

import java.time.LocalDateTime;

import com.example.auction_web.ChatBot.Enum.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotMessageResponse {
    String id;
    String conversationId;
    Role role;
    String content;
    LocalDateTime createdAt;
}
