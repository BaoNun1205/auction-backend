package com.example.auction_web.repository;

import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.SessionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.auth.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, String> {
    AuctionHistory findAuctionHistoryByAuctionSession_AuctionSessionId(String auctionSessionId);
    List<AuctionHistory> findAuctionHistorysByAuctionSession_AuctionSessionId(String auctionSessionId);


    @Query("SELECT MAX(a.bidPrice) FROM AuctionHistory a WHERE a.auctionSession.auctionSessionId = :auctionSessionId AND a.delFlag = false")
    BigDecimal findMaxBidPriceByAuctionSessionId(@Param("auctionSessionId") String auctionSessionId);

    @Query("SELECT new com.example.auction_web.dto.response.AuctionSessionInfoResponse(" +
            " COALESCE((SELECT COUNT(DISTINCT a.user.userId) FROM AuctionHistory a WHERE a.auctionSession.auctionSessionId = :auctionSessionId), 0), " +
            " COALESCE((SELECT COUNT(a) FROM AuctionHistory a WHERE a.auctionSession.auctionSessionId = :auctionSessionId), 0), " +
            " ah.user.userId, " +
            " COALESCE(MAX(ah.bidPrice), CAST(0 AS BigDecimal))) " +
            "FROM AuctionHistory ah " +
            "WHERE ah.auctionSession.auctionSessionId = :auctionSessionId " +
            "GROUP BY ah.user.userId " +
            "ORDER BY MAX(ah.bidPrice) DESC")
    List<AuctionSessionInfoResponse> findAuctionSessionInfo(@Param("auctionSessionId") String auctionSessionId);

    AuctionHistory findAuctionHistoryByAuctionSession_AuctionSessionIdAndUser_UserId(String auctionSessionId, String userId);

    @Query("SELECT new com.example.auction_web.dto.response.SessionHistoryResponse" +
            "(ah.user.userId, ah.bidPrice, ah.bidTime, ah.delFlag) " +
            "FROM AuctionHistory ah " +
            "WHERE ah.auctionSession.auctionSessionId = :auctionSessionId " +
            "ORDER BY ah.bidTime DESC")
    List<SessionHistoryResponse> findSessionHistoryByAuctionSessionId(@Param("auctionSessionId") String auctionSessionId);

    @Query("SELECT a.user.userId FROM AuctionHistory a WHERE a.bidPrice = (SELECT MAX(b.bidPrice) FROM AuctionHistory b WHERE b.auctionSession.auctionSessionId = :auctionSessionId)")
    String findMaxUserBidPrice(@Param("auctionSessionId") String auctionSessionId);
    @Query("SELECT a.user.userId FROM AuctionHistory a " +
            "WHERE a.auctionSession.auctionSessionId = :auctionSessionId " +
            "AND a.bidPrice = (SELECT MAX(b.bidPrice) FROM AuctionHistory b WHERE b.auctionSession.auctionSessionId = :auctionSessionId) " +
            "ORDER BY a.bidTime ASC")
    List<String> findTopUserBidPriceByBidTime(@Param("auctionSessionId") String auctionSessionId, Pageable pageable);
}