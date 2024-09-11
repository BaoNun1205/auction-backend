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
public class BillUpdateRequest {
    String addressId;
    BigDecimal depositPrice;
    BigDecimal totalProfitPrice;
    BigDecimal totalPrice;
    Boolean delFlag;
    LocalDateTime updatedAt;
}
