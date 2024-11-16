package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserResponse;
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
public class AuctionSessionResponse {
    String auctionSessionId;
    String typeSession;
    AssetResponse asset;
    UserResponse user;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal startingBids;
    BigDecimal bidIncrement;
    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
