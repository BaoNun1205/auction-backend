package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AuctionSessionRepository extends JpaRepository<AuctionSession, String> {

    List<AuctionSession> findAuctionSessionByStatusOrderByStartTimeAsc(String status);
}
