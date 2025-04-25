package com.example.auction_web.ChatBot.Mapper;

import org.mapstruct.Mapper;

import com.example.auction_web.ChatBot.Dto.BotConversationResponse;
import com.example.auction_web.ChatBot.Entity.BotConversation;

@Mapper(componentModel = "spring")
public interface BotConversationMapper {
    BotConversationResponse toConversationResponse(BotConversation conversation);
}
