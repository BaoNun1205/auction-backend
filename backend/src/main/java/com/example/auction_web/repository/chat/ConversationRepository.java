package com.example.auction_web.repository.chat;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.chat.Conversation;

import io.lettuce.core.dynamic.annotation.Param;

public interface ConversationRepository extends JpaRepository<Conversation, String> {
    List<Conversation> findConversationsByBuyer_UserIdOrSeller_UserId(String buyerId, String sellerId);
    List<Conversation> findConversationsByBuyerOrSeller(User buyer, User seller);

    @Query("SELECT c FROM Conversation c WHERE " +
       "(c.buyer.id = :userId1 AND c.seller.id = :userId2) OR " +
       "(c.buyer.id = :userId2 AND c.seller.id = :userId1)")
    Optional<Conversation> findConversationBetweenUsers(@Param("userId1") String userId1, @Param("userId2") String userId2);
}
