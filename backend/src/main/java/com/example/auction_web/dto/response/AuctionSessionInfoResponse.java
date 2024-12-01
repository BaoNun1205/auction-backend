package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionInfoResponse {
    String userId;
    Long totalBidder;
    Long totalAuctionHistory;
    BigDecimal highestBid;
    UserResponse user;

    public AuctionSessionInfoResponse(String userId, Long totalBidder, Long totalAuctionHistory, BigDecimal highestBid) {
        this.userId = userId;
        this.totalBidder = totalBidder;
        this.totalAuctionHistory = totalAuctionHistory;
        this.highestBid = highestBid;
    }

    public AuctionSessionInfoResponse(String userId, Long totalBidder, Long totalAuctionHistory, BigDecimal highestBid, UserResponse user) {
        this.userId = userId;
        this.totalBidder = totalBidder;
        this.totalAuctionHistory = totalAuctionHistory;
        this.highestBid = highestBid;
        this.user = user;
    }
}
