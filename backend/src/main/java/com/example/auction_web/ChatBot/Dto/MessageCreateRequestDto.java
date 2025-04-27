package com.example.auction_web.ChatBot.Dto;

import com.example.auction_web.ChatBot.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageCreateRequestDto {
    private String conversationId;
    private Role role;
    private String content;
}
