package com.example.auction_web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegisterAuctionCreateRequest {
    String userId;
    String auctionSessionId;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
