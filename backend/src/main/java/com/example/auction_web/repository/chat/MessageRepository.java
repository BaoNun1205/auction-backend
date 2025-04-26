package com.example.auction_web.repository.chat;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.auction_web.entity.chat.Message;

public interface MessageRepository extends JpaRepository<Message, String> {
    List<Message> findMessageByConversationId(String conversationId);
    // Lấy tất cả message đối phương gửi
    List<Message> findAllByConversationIdInAndSender_UserIdNotAndCreatedAtAfter(
        List<String> conversationIds, String userId, LocalDateTime createdAt);

    // Lấy tất cả message user gửi
    List<Message> findAllByConversationIdInAndSender_UserIdAndCreatedAtAfter(
        List<String> conversationIds, String userId, LocalDateTime createdAt);

}
