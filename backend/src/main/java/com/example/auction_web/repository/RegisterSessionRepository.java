package com.example.auction_web.repository;

import com.example.auction_web.entity.RegisterSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterSessionRepository extends JpaRepository<RegisterSession, String> {
    RegisterSession findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionId(String userId, String auctionSessionId);
    List<RegisterSession> findRegisterSessionByUser_UserId(String userId);
}
