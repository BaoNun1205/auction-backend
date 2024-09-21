package com.example.auction_web.repository;

import com.example.auction_web.entity.RegisterSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterSessionRepository extends JpaRepository<RegisterSession, String> {
    RegisterSession findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionId(String userId, String auctionSessionId);
}
