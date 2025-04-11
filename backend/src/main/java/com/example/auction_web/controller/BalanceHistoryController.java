package com.example.auction_web.controller;

import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.BalanceHistoryResponse;
import com.example.auction_web.service.BalanceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/balance-history")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BalanceHistoryController {
    BalanceHistoryService balanceHistoryService;

    @GetMapping("/user/{userId}")
    public ApiResponse<List<BalanceHistoryResponse>> getAllBalanceHistoriesByUserId(@PathVariable String userId) {
        return ApiResponse.<List<BalanceHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(balanceHistoryService.getAllBalanceHistoriesByUserId(userId))
                .build();
    }
}
