package com.example.auction_web.dto.response;

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
    BigDecimal highestBid;

    public AuctionSessionInfoResponse(Long totalBidder, Long totalAuctionHistory, BigDecimal highestBid) {
        this.totalBidder = totalBidder;
        this.totalAuctionHistory = totalAuctionHistory;
        this.highestBid = highestBid;
    }
}
