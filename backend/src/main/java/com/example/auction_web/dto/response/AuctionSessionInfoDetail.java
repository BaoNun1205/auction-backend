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
    String name;
    AssetResponse asset;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal startingBids;
    BigDecimal bidIncrement;
    String status;
    BigDecimal depositAmount;
    AuctionSessionInfoResponse auctionSessionInfo;

    public AuctionSessionInfoDetail(String id, String name, String description, LocalDateTime startTime, LocalDateTime endTime, BigDecimal startingBids, BigDecimal bidIncrement, String status, BigDecimal depositAmount) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startingBids = startingBids;
        this.bidIncrement = bidIncrement;
        this.status = status;
        this.depositAmount = depositAmount;
    }

}