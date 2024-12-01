package com.example.auction_web.dto.response;

import com.example.auction_web.entity.ImageAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionInfoDetail {
    String id;
    AssetResponse asset;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal startingBids;
    BigDecimal bidIncrement;
    String status;
    AuctionSessionInfoResponse auctionSessionInfo;

    public AuctionSessionInfoDetail(String id, String description, LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingBids, BigDecimal bidIncrement, String status) {
        this.id = id;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingBids = startingBids;
        this.bidIncrement = bidIncrement;
        this.status = status;
    }

}