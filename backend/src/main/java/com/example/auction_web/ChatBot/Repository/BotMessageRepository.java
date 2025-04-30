package com.example.auction_web.ChatBot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auction_web.ChatBot.Entity.BotMessage;

public interface BotMessageRepository extends JpaRepository<BotMessage, String> {
    List<BotMessage> findByConversationIdOrderByCreatedAtAsc(String conversationId);
    long countByConversationId(String conversationId);
}
