package com.example.auction_web.repository;

import com.example.auction_web.entity.Deposit;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
    List<Deposit> findDepositsByAuctionSession_AuctionSessionId(String auctionSessionId);
    List<Deposit> findDepositsByUser_UserId(String userId);

    @Query("SELECT MAX(d.depositPrice) FROM Deposit d WHERE d.auctionSession.auctionSessionId = :auctionSessionId")
    BigDecimal findMaxDepositPriceByAuctionSessionId(@Param("auctionSessionId") String auctionSessionId);
}
