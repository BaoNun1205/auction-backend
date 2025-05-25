package com.example.auction_web.dto.response;

import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.auth.User;
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
public class RegisterSessionResponse {
    String registerSessionId;
    User user;
    AuctionSessionResponse auctionSession;
    int totalRegistrations;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
