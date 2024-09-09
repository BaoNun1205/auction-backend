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
public class BillResponse {
    String billId;
    String userId;
    LocalDateTime billDate;
    String addressId;
    BigDecimal depositPrice;
    BigDecimal totalProfitPrice;
    BigDecimal totalPrice;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
