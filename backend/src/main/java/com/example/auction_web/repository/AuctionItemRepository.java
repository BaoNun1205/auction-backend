package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, String> {
    List<AuctionItem> findByAuctionSessionId(String auctionSessionId);
}
