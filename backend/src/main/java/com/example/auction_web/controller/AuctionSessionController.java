package com.example.auction_web.controller;

import com.example.auction_web.dto.request.*;
import com.example.auction_web.dto.response.*;
import com.example.auction_web.dto.response.statistical.AuctionSessionCountResponse;
import com.example.auction_web.enums.SESSION_WIN_STATUS;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.RegisterSessionService;
import com.example.auction_web.service.SessionWinnerService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/session")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionController {
    AuctionSessionService auctionSessionService;
    RegisterSessionService registerSessionService;
    SessionWinnerService sessionWinnerService;

    @PostMapping
    ApiResponse<AuctionSessionResponse> createAuctionSession(@RequestBody AuctionSessionCreateRequest request) {
        return ApiResponse.<AuctionSessionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.createAuctionSession(request))
                .build();
    }

    @PostMapping("/register")
    ApiResponse<RegisterSessionResponse> registerAuctionSession(@RequestBody RegisterSessionCreateRequest request) {
        return ApiResponse.<RegisterSessionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(registerSessionService.createRegisterSession(request))
                .build();
    }

    @PostMapping("/un-register")
    ApiResponse<String> unRegisterAuctionSession(@RequestBody RegisterSessionCreateRequest request) {
        registerSessionService.unRegisterSession(request);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Register deleted successfully")
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AuctionSessionResponse> updateAuctionSession(@PathVariable String id, @RequestBody AuctionSessionUpdateRequest request) {
        return ApiResponse.<AuctionSessionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.updateAuctionSession(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<DataResponse> filterAuctionSession(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String typeId,
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) LocalDateTime fromDate,
            @RequestParam(required = false) LocalDateTime toDate,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "true") Boolean isInCrease,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "20") Integer size) {

        List<AuctionSessionResponse> auctionSessionResponses = auctionSessionService.filterAuctionSession(status, typeId, userId, fromDate, toDate, minPrice, maxPrice, keyword, isInCrease, page, size);
        int total = auctionSessionService.totalAuctionSession(status, fromDate, toDate, keyword, isInCrease);

        return ApiResponse.<DataResponse>builder()
                .code(HttpStatus.OK.value())
                .result(DataResponse.<List<AuctionSessionResponse>>builder()
                        .data(auctionSessionResponses)
                        .total(total)
                        .build())
                .build();
    }

    @GetMapping("/byStatus/{status}")
    ApiResponse<List<AuctionSessionResponse>> getListAuctionSessionByStatus(@PathVariable String status) {
        return ApiResponse.<List<AuctionSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.getListAuctionSessionByStatus(status))
                .build();
    }

    @GetMapping("/{auctionSessionId}")
    ApiResponse<AuctionSessionInfoDetail> getAuctionSessionInfoDetail(@PathVariable String auctionSessionId) {
        return ApiResponse.<AuctionSessionInfoDetail>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.getDetailAuctionSessionById(auctionSessionId))
                .build();
    }

    @GetMapping("/asset/{assetId}")
    ApiResponse<AuctionSessionInfoDetail> getAuctionSessionInfoDetailByAssetId(@PathVariable String assetId) {
        return ApiResponse.<AuctionSessionInfoDetail>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.getDetailAuctionSessionByAssetId(assetId))
                .build();
    }

    @GetMapping("/related/{auctionSessionId}")
    ApiResponse<DataResponse> filterAuctionSessionRelated(@PathVariable String auctionSessionId) {
        List<AuctionSessionResponse> auctionSessionResponses = auctionSessionService.filterAuctionSessionRelated(auctionSessionId);
        return ApiResponse.<DataResponse>builder()
                .code(HttpStatus.OK.value())
                .result(DataResponse.<List<AuctionSessionResponse>>builder()
                        .data(auctionSessionResponses)
                        .total(auctionSessionResponses.size())
                        .build())
                .build();
    }

    @GetMapping("/registered/{userId}")
    ApiResponse<List<RegisterSessionResponse>> getRegisterSessionByUserId(@PathVariable String userId) {
        return ApiResponse.<List<RegisterSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(registerSessionService.getRegisterSessionByUserId(userId))
                .build();
    }

    @GetMapping("/check-register")
    ApiResponse<Boolean> checkRegister(@RequestParam String auctionSessionId,
                                      @RequestParam String userId) {
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(registerSessionService.getRegisterSessionByUserAndAuctionSession(userId, auctionSessionId))
                .build();
    }

    @GetMapping("/user-registered/{auctionSessionId}")
    ApiResponse<List<RegisterSessionResponse>> getRegisterSessionByAuctionSessionId(@PathVariable String auctionSessionId) {
        return ApiResponse.<List<RegisterSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(registerSessionService.getRegisterSessionByAuctionSessionId(auctionSessionId))
                .build();
    }

    @GetMapping("/win-sessions/{userId}")
    ApiResponse<List<SessionWinnerResponse>> getWinSessions(@PathVariable String userId) {
        return ApiResponse.<List<SessionWinnerResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(sessionWinnerService.getSessionsWinner(userId))
                .build();
    }

    @PutMapping("/win-sessions/{sessionWinnerId}/status")
    ApiResponse<SessionWinnerResponse> updateSessionWinnerStatus(@PathVariable String sessionWinnerId,
                                                                 @RequestParam SESSION_WIN_STATUS status) {
        return ApiResponse.<SessionWinnerResponse>builder()
                .code(HttpStatus.OK.value())
                .result(sessionWinnerService.updateSessionWinnerStatus(sessionWinnerId, status))
                .build();
    }
    
    @GetMapping("/active/count")
    ApiResponse<AuctionSessionCountResponse> countActiveAuctionSessions() {
        long totalCount = auctionSessionService.countAllActiveAuctionSessions();
        long countOfYear = auctionSessionService.countActiveAuctionSessionsByCurrentYear();
        double growthRate = auctionSessionService.getAuctionSessionGrowthRateThisYear();
        AuctionSessionCountResponse response = new AuctionSessionCountResponse(totalCount, countOfYear, growthRate);
        return ApiResponse.<AuctionSessionCountResponse>builder()
                .result(response)
                .build();
    }
}
