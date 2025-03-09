package com.example.auction_web.repository;

import com.example.auction_web.dto.response.AutoBidResponse;
import com.example.auction_web.entity.AutoBid;
import com.example.auction_web.enums.AUTOBID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AutoBidRepository extends JpaRepository<AutoBid, String> {
    @Query("SELECT a FROM AutoBid a WHERE a.auctionSession.auctionSessionId = :auctionSessionId AND a.status = :status")
    List<AutoBid> findAutoBidsByAuctionSession_AuctionSessionIdAndStatus(@Param("auctionSessionId") String auctionSessionId,
                                                                         @Param("status") AUTOBID status);
    boolean existsAutoBidByAuctionSession_AuctionSessionIdAndUser_UserIdAndStatus(String auctionSessionId, String userId, AUTOBID status);

    @Query("SELECT a FROM AutoBid a WHERE a.auctionSession.auctionSessionId = :auctionSessionId AND a.user.userId = :userId AND a.status = :status")
    AutoBid findAutoBidByAuctionSession_AuctionSessionIdAndUser_UserIdAndStatus(@Param("auctionSessionId") String auctionSessionId,
                                                                                                  @Param("userId") String userId,
                                                                                                  @Param("status") AUTOBID status);
}
