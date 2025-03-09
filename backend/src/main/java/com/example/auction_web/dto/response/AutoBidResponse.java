package com.example.auction_web.dto.response;

import com.example.auction_web.enums.AUTOBID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AutoBidResponse {
    String autoBidId;
    String userId;
    String auctionSessionId;
    BigDecimal maxBidPrice;
    BigDecimal bidIncrement;
    AUTOBID status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
