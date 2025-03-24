package com.example.auction_web.dto.response.chat;

import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.auth.User;

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
}
