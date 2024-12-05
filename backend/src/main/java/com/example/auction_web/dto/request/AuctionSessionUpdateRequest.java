package com.example.auction_web.dto.request;

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
public class AuctionSessionUpdateRequest {
    String typeSession;
    String name;
    String description;
    LocalDateTime startTime;
    LocalDateTime endTime;
    String status;
    BigDecimal bidIncrement;
    BigDecimal depositAmount;
}
