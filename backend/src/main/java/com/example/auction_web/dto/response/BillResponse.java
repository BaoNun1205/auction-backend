package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BillResponse {
    String billId;
    String transactionCode;
    LocalDateTime billDate;
    // String buyerId;
    // String sellerId;
    // String addressId;
    // String sessionId;
    UserResponse buyerBill;
    UserResponse sellerBill;
    AddressResponse address;
    AuctionSessionResponse session;
    BigDecimal totalPrice;
    BigDecimal bidPrice;
    BigDecimal depositPrice;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
