package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AutoBidCreateRequest;
import com.example.auction_web.dto.request.AutoBidUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AutoBidResponse;
import com.example.auction_web.service.AutoBidService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auto-bid")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AutoBidController {
    AutoBidService autoBidService;

    @PostMapping
    ApiResponse<AutoBidResponse> createAutoBid(@RequestBody AutoBidCreateRequest request) {
        return ApiResponse.<AutoBidResponse>builder()
                .code(HttpStatus.OK.value())
                .result(autoBidService.createAutoBid(request))
                .build();
    }

    @GetMapping("/check-auto-bid")
    ApiResponse<Boolean> checkAutoBid(@RequestParam String auctionSessionId, @RequestParam String userId) {
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(autoBidService.checkAutoBid(auctionSessionId, userId))
                .build();
    }

    @GetMapping
    ApiResponse<AutoBidResponse> getAutoBidByAuctionSessionIdAndUserId(@RequestParam String auctionSessionId, @RequestParam String userId) {
        return ApiResponse.<AutoBidResponse>builder()
                .code(HttpStatus.OK.value())
                .result(autoBidService.getAutoBidByAuctionSessionIdAndUserId(auctionSessionId, userId))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AutoBidResponse> updateAutoBid(@PathVariable String id, @RequestBody AutoBidUpdateRequest request) {
        return ApiResponse.<AutoBidResponse>builder()
                .code(HttpStatus.OK.value())
                .result(autoBidService.updateAutoBid(id, request))
                .build();
    }
}
