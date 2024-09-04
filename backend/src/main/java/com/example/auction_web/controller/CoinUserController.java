package com.example.auction_web.controller;

import com.example.auction_web.dto.request.CoinUserCreateRequest;
import com.example.auction_web.dto.request.CoinUserUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.CoinUserResponse;
import com.example.auction_web.service.CoinUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/coin-user")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class CoinUserController {
    CoinUserService coinUserService;

    @PostMapping
    ApiResponse<CoinUserResponse> crateCoinUser(@RequestBody CoinUserCreateRequest request) {
        return ApiResponse.<CoinUserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(coinUserService.createCoinUser(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<CoinUserResponse> updateCoinUser(@PathVariable String id, @RequestBody CoinUserUpdateRequest request) {
        return ApiResponse.<CoinUserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(coinUserService.updateCoinUser(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<CoinUserResponse>> getCoinUsers() {
        return ApiResponse.<List<CoinUserResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(coinUserService.getCoinUsers())
                .build();
    }
}
