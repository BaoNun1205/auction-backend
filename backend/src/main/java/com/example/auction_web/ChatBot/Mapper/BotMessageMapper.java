package com.example.auction_web.ChatBot.Mapper;

import org.mapstruct.Mapper;

import com.example.auction_web.ChatBot.Dto.BotMessageResponse;
import com.example.auction_web.ChatBot.Entity.BotMessage;

@Mapper(componentModel = "spring")
public interface BotMessageMapper {
    BotMessageResponse toMessageResponse(BotMessage message);
}
