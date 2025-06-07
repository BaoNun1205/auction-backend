package com.example.auction_web.dto.request;

import com.example.auction_web.enums.SESSION_WIN_STATUS;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static com.example.auction_web.utils.TransactionCodeGenerator.generateTransactionCode;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BillCreateRequest {
    String transactionCode = generateTransactionCode();
    LocalDateTime billDate;
    String buyerId;
    String sellerId;
    String addressId;
    String sessionId;
    BigDecimal totalPrice;
    BigDecimal bidPrice;
    BigDecimal depositPrice;
}
