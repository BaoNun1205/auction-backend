package com.example.auction_web.dto.response.chat;

import lombok.*;
import lombok.experimental.FieldDefaults;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class MessageResponse {
    private String id;
    private String content;
    private String timestamp;
    private String conversationId;
    private String senderId;
}
