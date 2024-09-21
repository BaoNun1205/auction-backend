package com.example.auction_web.WebSocket.controller;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.AuctionMessageResponse;
import com.example.auction_web.WebSocket.dto.response.DepositAddResponse;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;
import com.example.auction_web.WebSocket.service.RealTimeAuctionHandlerService;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.enums.AUCTION_STATUS;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Controller
@EnableScheduling
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RealTimeAuctionController {
    RealTimeAuctionHandlerService realTimeAuctionHandlerService;
    SimpMessagingTemplate simpleMessageTemplate;

    @MessageMapping("/rt-auction/join/{auctionSessionId}")
    @SendTo("/rt-product/auction-update/{auctionSessionId}")
    public ApiResponse<SessionJoinResponse> joinSession(@DestinationVariable String auctionSessionId, SessionJoinRequest request) {
        return ApiResponse.<SessionJoinResponse>builder()
                .code(HttpStatus.OK.value())
                .result(realTimeAuctionHandlerService.joinSession(request, auctionSessionId))
                .build();
    }

    @MessageMapping("/rt-auction/deposit/{auctionSessionId}")
    @SendTo("/rt-product/auction-update/{auctionSessionId}")
    public ApiResponse<DepositAddResponse> addDeposit(@DestinationVariable String auctionSessionId, DepositCreateRequest request) {
        return ApiResponse.<DepositAddResponse>builder()
                .code(HttpStatus.OK.value())
                .result(realTimeAuctionHandlerService.addDeposit(auctionSessionId, request))
                .build();
    }

    @Scheduled(fixedRate = 1000)
    public void sendCurrentTime() {
        try {
            List<AuctionSessionResponse> auctionSessionResponses = realTimeAuctionHandlerService.getAllAuctionSessions();
            AuctionMessageResponse auctionMessageResponse = AuctionMessageResponse.builder()
                    .status(AUCTION_STATUS.TIME_UPDATE)
                    .messageTime(Instant.now())
                    .build();
            for (AuctionSessionResponse auctionSessionResponse : auctionSessionResponses) {
                this.simpleMessageTemplate.convertAndSend("/rt-product/auction-update/" + auctionSessionResponse.getAuctionSessionId(), auctionMessageResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000)
    public void sendStartNotification() {
        try {
            List<AuctionSessionResponse> auctionSessionResponses = realTimeAuctionHandlerService.getAllAuctionSessions();
            AuctionMessageResponse auctionMessageResponse = AuctionMessageResponse.builder()
                    .status(AUCTION_STATUS.STARTED)
                    .messageTime(Instant.now())
                    .build();
            Instant now = Instant.now();
            Duration duration = Duration.ofSeconds(1); //1s
            for (AuctionSessionResponse auctionSessionResponse : auctionSessionResponses) {
                Instant startTime = auctionSessionResponse.getStartTime().atZone(ZoneId.systemDefault()).toInstant();
                Duration diff = Duration.between(now, startTime);

                if (Math.abs(diff.toMillis()) <= duration.toMillis()) {
                    this.simpleMessageTemplate.convertAndSend("/rt-product/auction-update/" + auctionSessionResponse.getAuctionSessionId(), auctionMessageResponse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(fixedRate = 1000)
    public void sendEndNotification() {
        try {
            List<AuctionSessionResponse> auctionSessionResponses = realTimeAuctionHandlerService.getAllAuctionSessions();
            AuctionMessageResponse auctionMessageResponse = AuctionMessageResponse.builder()
                    .status(AUCTION_STATUS.FINISHED)
                    .messageTime(Instant.now())
                    .build();

            Instant now = Instant.now();
            Duration duration = Duration.ofSeconds(1); //1s
            for (AuctionSessionResponse auctionSessionResponse : auctionSessionResponses) {
                Instant endTime = auctionSessionResponse.getEndTime().atZone(ZoneId.systemDefault()).toInstant();
                Duration diff = Duration.between(now, endTime);

                if (Math.abs(diff.toMillis()) <= duration.toMillis()) {
                    realTimeAuctionHandlerService.completeASession(auctionSessionResponse.getAuctionSessionId());
                    this.simpleMessageTemplate.convertAndSend("/rt-product/auction-update/" + auctionSessionResponse.getAuctionSessionId(), auctionMessageResponse);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
