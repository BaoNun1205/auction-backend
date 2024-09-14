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
public class BillCreateRequest {
    LocalDateTime billDate;
    String addressId;
    String depositId;
    BigDecimal bidPrice;
    BigDecimal profitPrice;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
