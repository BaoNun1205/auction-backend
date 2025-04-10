package com.example.auction_web.controller;

import com.example.auction_web.dto.request.BalanceUserCreateRequest;
import com.example.auction_web.dto.request.BalanceUserUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.service.BalanceUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/balance-user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BalanceUserController {
    BalanceUserService balanceUserService;

    @PostMapping
    ApiResponse<BalanceUserResponse> createBalanceUser(@RequestBody BalanceUserCreateRequest request) {
        return ApiResponse.<BalanceUserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(balanceUserService.createCoinUser(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<BalanceUserResponse> updateBalanceUser(@PathVariable String id, @RequestBody BalanceUserUpdateRequest request) {
        return ApiResponse.<BalanceUserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(balanceUserService.updateCoinUser(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<BalanceUserResponse>> getBalanceUsers() {
        return ApiResponse.<List<BalanceUserResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(balanceUserService.getCoinUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<BalanceUserResponse> getBalanceUserByUserId(@PathVariable String userId) {
        return ApiResponse.<BalanceUserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(balanceUserService.getCoinUserByUserId(userId))
                .build();
    }
}
