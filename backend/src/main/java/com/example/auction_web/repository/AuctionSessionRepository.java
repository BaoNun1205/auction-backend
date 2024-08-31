package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.AuctionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, String> {
}
