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
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SessionHistoryResponse {
    String userId;
    UserResponse user;
    BigDecimal bidPrice;
    LocalDateTime bidTime;
    Boolean delFlag;

    public SessionHistoryResponse(String userId, BigDecimal bidPrice, LocalDateTime bidTime, Boolean delFlag) {
        this.userId = userId;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime;
        this.delFlag = delFlag;
    }

    public SessionHistoryResponse(String userId, UserResponse user, BigDecimal bidPrice, LocalDateTime bidTime, Boolean delFlag) {
        this.userId = userId;
        this.user = user;
        this.bidPrice = bidPrice;
        this.bidTime = bidTime;
        this.delFlag = delFlag;
    }
}
