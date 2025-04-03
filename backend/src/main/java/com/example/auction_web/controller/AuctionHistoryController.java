package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.request.AuctionSessionInfoRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.SessionHistoryResponse;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.service.AuctionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auction-history")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionHistoryController {
    AuctionHistoryService auctionHistoryService;

    @PostMapping
    ResponseEntity<ApiResponse<AuctionHistoryResponse>> createAuctionHistory(@RequestBody AuctionHistoryCreateRequest request) {
        try {
            return ResponseEntity.ok(
                    ApiResponse.<AuctionHistoryResponse>builder()
                            .code(HttpStatus.OK.value())
                            .result(auctionHistoryService.createAuctionHistory(request))
                            .build()
            );
        } catch (AppException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ApiResponse.<AuctionHistoryResponse>builder()
                            .code(HttpStatus.BAD_REQUEST.value())
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("/{id}")
    ApiResponse<AuctionHistoryResponse> updateAuctionHistory(@PathVariable String id, @RequestBody AuctionHistoryUpdateRequest request) {
        return ApiResponse.<AuctionHistoryResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.updateAuctionHistory(id, request))
                .build();
    }

    @GetMapping("/{auctionSessionId}")
    ApiResponse<List<SessionHistoryResponse>> getAuctionHistoriesByAuctionSessionId(@PathVariable String auctionSessionId) {
        return ApiResponse.<List<SessionHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.getSessionsHistoryByAuctionSessionId(auctionSessionId))
                .build();
    }

    @GetMapping("/info-auctions-session/{auctionSessionId}")
    ApiResponse<AuctionSessionInfoResponse> getAuctionSessionInfo(@PathVariable String auctionSessionId) {
        return ApiResponse.<AuctionSessionInfoResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.getAuctionSessionInfo(auctionSessionId))
                .build();
    }
}
