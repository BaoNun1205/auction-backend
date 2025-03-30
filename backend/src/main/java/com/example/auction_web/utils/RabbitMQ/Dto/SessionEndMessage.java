package com.example.auction_web.utils.RabbitMQ.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class SessionEndMessage {
    String AuctionSessionId;
    String AuctionSessionWinnerId;
}
