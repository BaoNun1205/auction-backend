package com.example.auction_web.repository;

import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, String> {
    AuctionHistory findAuctionHistoryByAuctionSession_AuctionSessionId(String auctionSessionId);
    int countAuctionHistoriesByAuctionSession_AuctionSessionId(String auctionSessionId);

    @Query("SELECT MAX(bidPrice) FROM AuctionHistory WHERE auctionSession.auctionSessionId = :auctionSessionId")
    BigDecimal findMaxBidPriceByAuctionSession(String auctionSessionId);
}
