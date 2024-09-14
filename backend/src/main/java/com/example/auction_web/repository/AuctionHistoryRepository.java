package com.example.auction_web.repository;

import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionHistoryRepository extends JpaRepository<AuctionHistory, String> {
    AuctionHistory findAuctionHistoryByAuctionSession_AuctionSessionId(String auctionSessionId);
}
