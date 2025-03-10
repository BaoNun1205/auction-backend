package com.example.auction_web.repository.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auction_web.entity.chat.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findMessageByConversationId(String conversationId);
}
