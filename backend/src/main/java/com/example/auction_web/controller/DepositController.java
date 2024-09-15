package com.example.auction_web.controller;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposit")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class DepositController {
    DepositService depositService;

    @PostMapping
    ApiResponse<DepositResponse> createDeposit(@RequestBody DepositCreateRequest request) {
        return ApiResponse.<DepositResponse>builder()
                .code(HttpStatus.OK.value())
                .result(depositService.createDeposit(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<DepositResponse> updateDeposit(@PathVariable String id, @RequestBody DepositUpdateRequest request) {
        return ApiResponse.<DepositResponse>builder()
                .code(HttpStatus.OK.value())
                .result(depositService.updateDeposit(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<DepositResponse>> findAllDeposits() {
        return ApiResponse.<List<DepositResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(depositService.findAllDeposits())
                .build();
    }

    @GetMapping("/auction-item/id/{auctionSessionId}")
    ApiResponse<List<DepositResponse>> findDepositByAuctionItemId(@PathVariable String auctionSessionId) {
        return ApiResponse.<List<DepositResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(depositService.findDepositByAuctionSessionId(auctionSessionId))
                .build();
    }

    @GetMapping("/user/id/{userId}")
    ApiResponse<List<DepositResponse>> findDepositByUserId(@PathVariable String userId) {
        return ApiResponse.<List<DepositResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(depositService.findDepositByUserId(userId))
                .build();
    }
}
