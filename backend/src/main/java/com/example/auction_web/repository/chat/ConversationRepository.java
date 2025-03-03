package com.example.auction_web.repository.chat;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auction_web.entity.chat.Conversation;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
    List<Conversation> findByBuyer_UserIdOrSeller_UserId(String buyerId, String sellerId);
}
