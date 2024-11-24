package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.request.AuctionSessionInfoRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.service.AuctionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auction-history")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionHistoryController {
    AuctionHistoryService auctionHistoryService;

    @PostMapping
    ApiResponse<AuctionHistoryResponse> createAuctionHistory(@RequestBody AuctionHistoryCreateRequest request) {
        return ApiResponse.<AuctionHistoryResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.createAuctionHistory(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AuctionHistoryResponse> updateAuctionHistory(@PathVariable String id, @RequestBody AuctionHistoryUpdateRequest request) {
        return ApiResponse.<AuctionHistoryResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.updateAuctionHistory(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AuctionHistoryResponse>> getAllAuctionHistories() {
        return ApiResponse.<List<AuctionHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.getAllAuctionHistories())
                .build();
    }

    @GetMapping("/auctionSession/{auctionSessionId}")
    ApiResponse<AuctionHistoryResponse> getAuctionHistoriesByAuctionItemId(@PathVariable String auctionSessionId) {
        return ApiResponse.<AuctionHistoryResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.getAuctionHistoriesByAuctionSessionId(auctionSessionId))
                .build();
    }

    @GetMapping("/infoAuctionSession/{auctionSessionId}")
    ApiResponse<AuctionSessionInfoResponse> getAuctionSessionInfo(@PathVariable String auctionSessionId) {
        return ApiResponse.<AuctionSessionInfoResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.getAuctionSessionInfo(auctionSessionId))
                .build();
    }

    @GetMapping("/checkDeposit")
    ApiResponse<Boolean> checkDeposit(@RequestBody AuctionHistoryCreateRequest request) {
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryService.checkDeposit(request.getUserId(), request.getAuctionSessionId()))
                .build();
    }
}
