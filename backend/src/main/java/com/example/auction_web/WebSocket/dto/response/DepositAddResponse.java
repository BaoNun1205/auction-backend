package com.example.auction_web.WebSocket.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DepositAddResponse {
    String depositId;
    String userId;
    String name;
    String auctionSessionId;
    String typeAuctionSession;
    String depositPrice;
}
