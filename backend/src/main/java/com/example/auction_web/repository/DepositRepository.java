package com.example.auction_web.repository;

import com.example.auction_web.dto.response.UsersJoinSessionResponse;
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

    @Query("SELECT d FROM Deposit d WHERE d.auctionSession.auctionSessionId = :auctionSessionId AND d.delFlag = false")
    List<Deposit> findActiveDepositsByAuctionSessionIdAndDelFlag(@Param("auctionSessionId") String auctionSessionId);

    Deposit findByAuctionSession_AuctionSessionIdAndUser_UserId(String auctionSessionId, String userId);

    @Query("SELECT MAX(d.depositPrice) FROM Deposit d WHERE d.auctionSession.auctionSessionId = :auctionSessionId")
    BigDecimal findMaxDepositPriceByAuctionSessionId(@Param("auctionSessionId") String auctionSessionId);

    @Query("SELECT new com.example.auction_web.dto.response.UsersJoinSessionResponse(d.auctionSession.auctionSessionId) FROM Deposit d WHERE d.user.userId = :userId")
    List<UsersJoinSessionResponse> findSessionsJoinByUserId(@Param("userId") String userId);
}
