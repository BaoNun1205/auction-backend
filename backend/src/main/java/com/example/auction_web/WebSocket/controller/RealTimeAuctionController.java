package com.example.auction_web.WebSocket.controller;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;
import com.example.auction_web.WebSocket.service.RealTimeAuctionHandlerService;
import com.example.auction_web.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Controller
@EnableScheduling
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RealTimeAuctionController {
    RealTimeAuctionHandlerService realTimeAuctionHandlerService;

    @MessageMapping("/rt-auction/join/{auctionSessionId}")
    @SendTo("/rt-product/auction-update/{auctionSessionId}")
    public ApiResponse<SessionJoinResponse> joinSession(@DestinationVariable String auctionSessionId, SessionJoinRequest request) {
        return ApiResponse.<SessionJoinResponse>builder()
                .code(HttpStatus.OK.value())
                .result(realTimeAuctionHandlerService.joinSession(request, auctionSessionId))
                .build();
    }
}
