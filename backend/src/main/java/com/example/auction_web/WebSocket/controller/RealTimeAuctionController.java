package com.example.auction_web.WebSocket.controller;

import com.example.auction_web.WebSocket.dto.request.SessionJoinRequest;
import com.example.auction_web.WebSocket.dto.response.AuctionMessageResponse;
import com.example.auction_web.WebSocket.dto.response.DepositAddResponse;
import com.example.auction_web.WebSocket.dto.response.SessionJoinResponse;
import com.example.auction_web.WebSocket.service.RealTimeAuctionHandlerService;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoDetail;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.service.AuctionSessionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Controller
@EnableScheduling
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class RealTimeAuctionController {
    RealTimeAuctionHandlerService realTimeAuctionHandlerService;
    AuctionSessionService auctionSessionService;
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

    @MessageMapping("/rt-auction/placeBid/{auctionSessionId}")
    @SendTo("/rt-product/bidPrice-update/{auctionSessionId}")
    ApiResponse<AuctionSessionInfoDetail> getAuctionSessionInfoDetail(@DestinationVariable String auctionSessionId) {
        return ApiResponse.<AuctionSessionInfoDetail>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.getDetailAuctionSessionById(auctionSessionId))
                .build();
    }



//    @Scheduled(fixedRate = 1000)
//    public void sendCurrentTime() {
//        try {
//            List<AuctionSessionResponse> auctionSessionResponses = realTimeAuctionHandlerService.getAllAuctionSessions();
//            AuctionMessageResponse auctionMessageResponse = AuctionMessageResponse.builder()
//                    .status(AUCTION_STATUS.TIME_UPDATE.toString())
//                    .messageTime(LocalDateTime.now())
//                    .build();
//            for (AuctionSessionResponse auctionSessionResponse : auctionSessionResponses) {
//                this.simpleMessageTemplate.convertAndSend("/rt-product/auction-update/" + auctionSessionResponse.getAuctionSessionId(), auctionMessageResponse);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Scheduled(fixedRate = 1000)
//    public void sendStartNotification() {
//        try {
//            List<AuctionSessionResponse> auctionSessionResponses = realTimeAuctionHandlerService.getAllAuctionSessions();
//            AuctionMessageResponse auctionMessageResponse = AuctionMessageResponse.builder()
//                    .status(AUCTION_STATUS.STARTED.toString())
//                    .messageTime(LocalDateTime.now())
//                    .build();
//            LocalDateTime now = LocalDateTime.now(); // Lấy thời gian hiện tại
//
//            for (AuctionSessionResponse auctionSessionResponse : auctionSessionResponses) {
//                LocalDateTime startTime = auctionSessionResponse.getStartTime(); // Lấy thời gian bắt đầu từ auctionSessionResponse
//                long diffInMillis = Math.abs(ChronoUnit.MILLIS.between(now, startTime)); // Tính chênh lệch thời gian
//
//                if (diffInMillis <= 1000) { // Kiểm tra nếu chênh lệch nhỏ hơn hoặc bằng 1 giây
//                    this.simpleMessageTemplate.convertAndSend("/rt-product/auction-update/" + auctionSessionResponse.getAuctionSessionId(), auctionMessageResponse);
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Scheduled(fixedRate = 1000)
//    public void sendEndNotification() {
//        try {
//            List<AuctionSessionResponse> auctionSessionResponses = realTimeAuctionHandlerService.getAllAuctionSessions();
//            AuctionMessageResponse auctionMessageResponse = AuctionMessageResponse.builder()
//                    .status(AUCTION_STATUS.FINISHED.toString())
//                    .messageTime(LocalDateTime.now())
//                    .build();
//
//            LocalDateTime now = LocalDateTime.now(); // Lấy thời gian hiện tại
//            for (AuctionSessionResponse auctionSessionResponse : auctionSessionResponses) {
//                LocalDateTime startTime = auctionSessionResponse.getStartTime(); // Lấy thời gian bắt đầu từ auctionSessionResponse
//                long diffInMillis = Math.abs(ChronoUnit.MILLIS.between(now, startTime)); // Tính chênh lệch thời gian
//
//                if (diffInMillis <= 1000) {
//                    this.simpleMessageTemplate.convertAndSend("/rt-product/auction-update/" + auctionSessionResponse.getAuctionSessionId(), auctionMessageResponse);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
