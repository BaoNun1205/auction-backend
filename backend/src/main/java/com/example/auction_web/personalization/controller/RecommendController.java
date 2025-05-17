package com.example.auction_web.personalization.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.personalization.service.RecommendService;

@RestController
@RequestMapping("/recommend")
public class RecommendController {

    @Autowired
    RecommendService recommendService;

    @GetMapping("/{userId}")
    public ApiResponse<List<AuctionSessionResponse>> recommendByUser(
        @PathVariable String userId,
        @RequestParam(required = false) String status) {
        return ApiResponse.<List<AuctionSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(recommendService.recommendAuctionSessionResponses(userId, status))
                .build();
    }

    @GetMapping("/session/{sessionId}")
    public ApiResponse<List<AuctionSessionResponse>> recommendBySession(@PathVariable String sessionId) {
        return ApiResponse.<List<AuctionSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(recommendService.recommendAuctionSessionResponsesByAuctionSessionId(sessionId))
                .build();
    }
}
