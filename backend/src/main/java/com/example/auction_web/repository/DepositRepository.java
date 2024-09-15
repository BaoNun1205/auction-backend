package com.example.auction_web.repository;

import com.example.auction_web.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
    List<Deposit> findDepositsByAuctionSession_AuctionSessionId(String auctionSessionId);
    List<Deposit> findDepositsByUser_UserId(String userId);
}
