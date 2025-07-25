package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserInfoBase;
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
    Long totalBidder;
    Long totalAuctionHistory;
    String userId;
    BigDecimal highestBid;
    UserInfoBase user;
    int totalRegistrations;

    public AuctionSessionInfoResponse(Long totalBidder, Long totalAuctionHistory, String userId, BigDecimal highestBid) {
        this.totalBidder = totalBidder;
        this.totalAuctionHistory = totalAuctionHistory;
        this.userId = userId;
        this.highestBid = highestBid;
    }

    public AuctionSessionInfoResponse(Long totalBidder, Long totalAuctionHistory, String userId, BigDecimal highestBid, UserInfoBase user) {
        this.totalBidder = totalBidder;
        this.totalAuctionHistory = totalAuctionHistory;
        this.userId = userId;
        this.highestBid = highestBid;
        this.user = user;
    }

        public AuctionSessionInfoResponse(Long totalBidder, Long totalAuctionHistory, String userId, BigDecimal highestBid, UserInfoBase user, int totalRegistrations) {
        this.totalBidder = totalBidder;
        this.totalAuctionHistory = totalAuctionHistory;
        this.userId = userId;
        this.highestBid = highestBid;
        this.user = user;
        this.totalRegistrations = totalRegistrations;
    }
}
