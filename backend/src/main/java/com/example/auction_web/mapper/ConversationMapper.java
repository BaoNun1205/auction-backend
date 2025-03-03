package com.example.auction_web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.entity.chat.Conversation;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    @Mapping(source = "buyer.userId", target = "buyerId")
    @Mapping(source = "seller.userId", target = "sellerId")
    ConversationResponse toConversationResponse(Conversation conversation);
}
