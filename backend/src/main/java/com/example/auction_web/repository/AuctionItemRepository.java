package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.AuctionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionItemRepository extends JpaRepository<AuctionItem, String> {
    List<AuctionItem> findByAuctionSession(AuctionSession auctionSession);
}
