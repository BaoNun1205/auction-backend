package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionHistoryDetailResponse {
    String auctionHistoryDetailId;
    String auctionHistoryId;
    String userId;
    BigDecimal auctionPrice;
    LocalDateTime auctionTime;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
