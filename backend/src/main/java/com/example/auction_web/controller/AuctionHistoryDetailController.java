package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AuctionHistoryDetailCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryDetailDeleteRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionHistoryDetailResponse;
import com.example.auction_web.service.AuctionHistoryDetailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auction-history-details")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionHistoryDetailController {
    AuctionHistoryDetailService auctionHistoryDetailService;

    @PostMapping
    ApiResponse<AuctionHistoryDetailResponse> createAuctionHistoryDetail(AuctionHistoryDetailCreateRequest request) {
        return ApiResponse.<AuctionHistoryDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryDetailService.createAuctionHistoryDetail(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AuctionHistoryDetailResponse> deleteAuctionHistoryDetail(String id, AuctionHistoryDetailDeleteRequest request) {
        return ApiResponse.<AuctionHistoryDetailResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryDetailService.deleteAuctionHistoryDetail(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AuctionHistoryDetailResponse>> getAllAuctionHistoryDetails() {
        return ApiResponse.<List<AuctionHistoryDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryDetailService.getAllAuctionHistoryDetails())
                .build();
    }

    @GetMapping("/auction-history/id/{auctionHistoryId}")
    ApiResponse<List<AuctionHistoryDetailResponse>> getAllByAuctionHistoryID(@PathVariable String auctionHistoryId) {
        return ApiResponse.<List<AuctionHistoryDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryDetailService.getAllByAuctionHistoryID(auctionHistoryId))
                .build();
    }

    @GetMapping("/user/id/{userId}")
    ApiResponse<List<AuctionHistoryDetailResponse>> getAllByUserId(@PathVariable String userId) {
        return ApiResponse.<List<AuctionHistoryDetailResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionHistoryDetailService.getAllByUserId(userId))
                .build();
    }
}
