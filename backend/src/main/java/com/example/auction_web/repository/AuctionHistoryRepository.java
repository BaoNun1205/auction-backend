package com.example.auction_web.repository;

import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.entity.AuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, String> {
    AuctionHistory findAuctionHistoryByAuctionSession_AuctionSessionId(String auctionSessionId);

    @Query("SELECT MAX(a.bidPrice) FROM AuctionHistory a WHERE a.auctionSession.auctionSessionId = :auctionSessionId AND a.delFlag = false")
    BigDecimal findMaxBidPriceByAuctionSessionId(@Param("auctionSessionId") String auctionSessionId);

    @Query("SELECT new com.example.auction_web.dto.response.AuctionSessionInfoResponse(" +
            "COUNT(DISTINCT ah.user.userId), " +
            "COUNT(ah), " +
            "COALESCE(MAX(ah.bidPrice), CAST(0 AS BigDecimal))) " +
            "FROM AuctionHistory ah " +
            "WHERE ah.auctionSession.auctionSessionId = :auctionSessionId")
    AuctionSessionInfoResponse findAuctionSessionInfo(@Param("auctionSessionId") String auctionSessionId);

    AuctionHistory findAuctionHistoryByAuctionSession_AuctionSessionIdAndUser_UserId(String auctionSessionId, String userId);
}