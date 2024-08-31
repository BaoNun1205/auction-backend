package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionResponse {
    String AuctionSessionId;
    String typeSession;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
