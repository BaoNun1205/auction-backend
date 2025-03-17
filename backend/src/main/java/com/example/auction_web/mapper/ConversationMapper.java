package com.example.auction_web.mapper;

import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.auction_web.dto.response.chat.ConversationResponse;
import com.example.auction_web.entity.chat.Conversation;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ConversationMapper {
    @Mapping(source = "buyer", target = "buyerId", qualifiedByName = "userToString")
    @Mapping(source = "seller", target = "sellerId", qualifiedByName = "userToString")
    ConversationResponse toConversationResponse(Conversation conversation);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
