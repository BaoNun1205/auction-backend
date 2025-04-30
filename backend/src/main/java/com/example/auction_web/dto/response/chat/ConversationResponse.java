package com.example.auction_web.dto.response.chat;

import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ConversationResponse {
    private String conversationId;
    private String name;
    private String lastMessage;
    private String time;
    private int unread;
    private UserResponse buyer;
    private UserResponse seller;
    private LocalDateTime updatedAt;
}
