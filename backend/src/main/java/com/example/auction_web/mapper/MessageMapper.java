package com.example.auction_web.mapper;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.chat.Conversation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.auction_web.dto.response.chat.MessageResponse;
import com.example.auction_web.entity.chat.Message;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    @Mapping(source = "sender", target = "sender")
    MessageResponse toMessageResponse(Message message);
}
