package com.example.auction_web.controller;

import com.example.auction_web.dto.request.PaymentSessionDTO;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.BalanceHistoryResponse;
import com.example.auction_web.service.BalanceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

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

    @PostMapping("/payment-session")
    public ApiResponse<String> paymentSession(@RequestBody PaymentSessionDTO request) {

            balanceHistoryService.paymentSession(request.getBuyerId(), request.getSellerId(), request.getSessionId(), request.getAddressId());
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .result("Thanh toán thành công")
                    .build();

    }

    @PostMapping("/completed-payment-session")
    public ApiResponse<String> completedPaymentSession(@RequestBody PaymentSessionDTO request) {

            balanceHistoryService.comletedPaymentSession(request.getBuyerId(), request.getSellerId(), request.getSessionId());
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .result("Hoàn tất thanh toán")
                    .build();

    }

    @PostMapping("/cancel-payment-session")
    public ApiResponse<String> cancelPaymentSession(@RequestBody PaymentSessionDTO request) {
        try {
            balanceHistoryService.cancelSession(request.getSellerId(), request.getSessionId());
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .result("Hủy thanh toán thành công")
                    .build();
        } catch (Exception ex) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.BAD_REQUEST.value())
                    .message(ex.getMessage())
                    .build();
        }
    }
}
