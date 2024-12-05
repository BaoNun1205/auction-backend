package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UsersJoinSessionResponse {
    String sessionId;
    AuctionSessionResponse auctionSession;

    public UsersJoinSessionResponse(String sessionId) {
        this.sessionId = sessionId;
    }

    public UsersJoinSessionResponse(String sessionId, AuctionSessionResponse auctionSession) {
        this.sessionId = sessionId;
        this.auctionSession = auctionSession;
    }
}
