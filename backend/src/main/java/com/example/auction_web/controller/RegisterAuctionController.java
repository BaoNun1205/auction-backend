package com.example.auction_web.controller;

import com.example.auction_web.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/register-auction")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RegisterAuctionController {
    RegisterAuctionService registerAuctionService;

    @PostMapping
    ApiResponse<RegisterAuctionResponse> createRegisterAuction(@RequestBody RegisterAuctionCreateRequest request) {
        return ApiResponse.<RegisterAuctionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(registerAuctionService.createRegisterAuction(request))
                .build();
    }

    @PutMapping("/{registerAuctionId}")
    ApiResponse<RegisterAuctionResponse> updateRegisterAuction(@PathVariable String registerAuctionId, @RequestBody RegisterAuctionUpdateRequest request) {
        return ApiResponse.<RegisterAuctionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(registerAuctionService.updateRegisterAuction(registerAuctionId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RegisterAuctionResponse>> getRegisterAuctions() {
        return ApiResponse.<List<RegisterAuctionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(registerAuctionService.findAllRegisterAuctions())
                .build();
    }

    @GetMapping("/user/{userId}")
    ApiResponse<List<RegisterAuctionResponse>> getRegisterAuctionByUserId(@PathVariable String userId) {
        return ApiResponse.<List<RegisterAuctionResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(registerAuctionService.findRegisterAuctionByUserId(userId))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<RegisterAuctionResponse> getRegisterAuctionById(@PathVariable String id) {
        return ApiResponse.<RegisterAuctionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(registerAuctionService.findRegisterAuctionById(id))
                .build();
    }

    @GetMapping("/auction-session/{auctionSessionId}")
    ApiResponse<RegisterAuctionResponse> getRegisterAuctionByAuctionSessionId(@PathVariable String auctionSessionId) {
        return ApiResponse.<RegisterAuctionResponse>builder()
                .code(HttpStatus.OK.value())
                .result(registerAuctionService.findRegisterAuctionByAuctionSessionId(auctionSessionId))
                .build();
    }
}
