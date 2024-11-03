package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AuctionSessionByStatusRequest;
import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.RegisterSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.RegisterSessionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auction-session")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionISessionController {
    AuctionSessionService auctionSessionService;
    RegisterSessionService registerSessionService;

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

    @PutMapping("/{id}")
    ApiResponse<AuctionSessionResponse> updateAuctionSeesion(@PathVariable String id, @RequestBody AuctionSessionUpdateRequest request) {
        return ApiResponse.<AuctionSessionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.updateAuctionSession(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AuctionSessionResponse>> getAllAuctionSessions() {
        return ApiResponse.<List<AuctionSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.getAllAuctionSessions())
                .build();
    }

    @PatchMapping()
    ApiResponse<List<AuctionSessionResponse>> getListAuctionSessionByStatus(@RequestBody AuctionSessionByStatusRequest request) {
        return ApiResponse.<List<AuctionSessionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionSessionService.getListAuctionSessionByStatus(request.getStatus()))
                .build();
    }
}
