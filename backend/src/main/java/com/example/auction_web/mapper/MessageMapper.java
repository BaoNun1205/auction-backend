package com.example.auction_web.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.auction_web.dto.response.chat.MessageResponse;
import com.example.auction_web.entity.chat.Message;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender.userId", target = "senderId")
    @Mapping(source = "conversation.id", target = "conversationId")
    MessageResponse toMessageResponse(Message message);
}
