package com.example.auction_web.repository;

import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.RegisterAuction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterAuctionRepository extends JpaRepository<RegisterAuction, String> {
    List<RegisterAuction> findRegisterAuctionsByUser_UserId(String userId);
    RegisterAuction findRegisterAuctionByAuctionSession_AuctionSessionId(String auctionSessionId);
}
