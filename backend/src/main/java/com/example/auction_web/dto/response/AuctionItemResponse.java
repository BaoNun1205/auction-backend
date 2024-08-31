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
public class AuctionItemResponse {
    String auctionItemId;
    String auctionSessionId;
    String assetId;
    BigDecimal startingBids;
    BigDecimal bidIncrement;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
