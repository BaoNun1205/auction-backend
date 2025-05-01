package com.example.auction_web.ChatBot.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.auction_web.ChatBot.Entity.BotConversation;

public interface BotConversationRepository extends JpaRepository<BotConversation, String> {
    List<BotConversation> findByUser_UserId(String userId);
}
