package com.example.auction_web.repository;

import com.example.auction_web.entity.RegisterSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterSessionRepository extends JpaRepository<RegisterSession, String> {
    RegisterSession findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionIdAndDelFlagIsFalse(String userId, String auctionSessionId);

    RegisterSession findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionIdAndDelFlagIsTrue(String userId, String auctionSessionId);

    List<RegisterSession> findRegisterSessionByUser_UserIdAndDelFlagIsFalse(String userId);

    List<RegisterSession> findRegisterSessionByAuctionSession_AuctionSessionIdAndDelFlagIsFalse(String auctionSessionId);

    int countRegisterSessionsByAuctionSession_AuctionSessionId(String auctionSessionId);
}
