package com.example.auction_web.repository;

import com.example.auction_web.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
    List<Deposit> findDepositsByAuctionItem_AuctionItemId(String auctionItemID);
    List<Deposit> findDepositsByUser_UserId(String userId);
}
