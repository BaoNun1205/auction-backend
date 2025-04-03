package com.example.auction_web.mapper;

import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.entity.chat.Conversation;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    ConversationResponse toConversationResponse(Conversation conversation);
}
