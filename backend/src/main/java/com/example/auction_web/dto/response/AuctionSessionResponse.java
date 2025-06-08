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
    String name;
    String description;
    String typeSession;
    AssetResponse asset;
    LocalDateTime startTime;
    LocalDateTime endTime;
    BigDecimal startingBids;
    BigDecimal bidIncrement;
    BigDecimal depositAmount;
    String status;
    Boolean delFlag;
    AuctionSessionInfoResponse auctionSessionInfo;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
