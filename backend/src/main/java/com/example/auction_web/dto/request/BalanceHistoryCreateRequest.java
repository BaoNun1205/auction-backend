package com.example.auction_web.dto.request;

import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.enums.ACTIONBALANCE;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BalanceHistoryCreateRequest {
    String balanceUserId;
    BigDecimal amount;
    String description;
    ACTIONBALANCE actionbalance;
}
