package com.example.auction_web.dto.request;

import com.example.auction_web.enums.AUTOBID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AutoBidCreateRequest {
    String userId;
    String auctionSessionId;
    BigDecimal maxBidPrice;
    BigDecimal bidIncrement;
    AUTOBID status;
}
