package com.example.auction_web.ChatBot.Dto;

import java.time.LocalDateTime;

import com.example.auction_web.ChatBot.Enum.SenderType;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BotMessageResponse {
    String id;
    String conversationId;
    SenderType sender;
    String content;
    LocalDateTime createdAt;
}
