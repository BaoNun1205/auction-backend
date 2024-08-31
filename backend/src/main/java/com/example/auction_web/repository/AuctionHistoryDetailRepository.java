package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionHistoryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionHistoryDetailRepository extends JpaRepository<AuctionHistoryDetail, String> {
    List<AuctionHistoryDetail> findAllByAuctionHistoryId(String auctionHistoryId);
}
