package com.example.auction_web.repository;

import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.CoinUser;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
    List<Deposit> findDepositByAuctionItem(AuctionItem auctionItem);
    List<Deposit> findDepositByUser(User user);
}
