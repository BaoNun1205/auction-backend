package com.example.auction_web.dto.response.chat;

import com.example.auction_web.entity.auth.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ConversationResponse {
    private String id;
    private String name;
    private String lastMessage;
    private String time;
    private int unread;
    private String buyerId;
    private String sellerId;
}
