package com.example.auction_web.controller;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.service.BillService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BillController {
    BillService billService;

    @PostMapping
    ApiResponse<BillResponse> createBill(@RequestBody BillCreateRequest request) {
        return ApiResponse.<BillResponse>builder()
                .code(HttpStatus.OK.value())
                .result(billService.createBill(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<BillResponse> updateBill(@PathVariable String id, @RequestBody BillUpdateRequest request) {
        return ApiResponse.<BillResponse>builder()
                .code(HttpStatus.OK.value())
                .result(billService.updateBill(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<BillResponse>> getAllBills() {
        return ApiResponse.<List<BillResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(billService.getAllBills())
                .build();
    }

    @GetMapping("/buyerId/{buyerId}")
    ApiResponse<List<BillResponse>> getBillsByBuyerId(@PathVariable String buyerId) {
        try {
            return ApiResponse.<List<BillResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .result(billService.getBillByBuyerBillId(buyerId))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<BillResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .result(null)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/sellerId/{sellerId}")
    ApiResponse<List<BillResponse>> getBillsBySellerId(@PathVariable String sellerId) {
        try {
            return ApiResponse.<List<BillResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .result(billService.getBillByBuyerBillId(sellerId))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<List<BillResponse>>builder()
                    .code(HttpStatus.OK.value())
                    .result(null)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/{id}")
    ApiResponse<BillResponse> getBillById(@PathVariable String id) {
        try {
            return ApiResponse.<BillResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(billService.getBillById(id))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<BillResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(null)
                    .message(e.getMessage())
                    .build();
        }
    }

    @GetMapping("/session/{sessionId}")
    ApiResponse<BillResponse> getBillBySessionId(@PathVariable String sessionId) {
        try {
            return ApiResponse.<BillResponse>builder()
                    .code(HttpStatus.OK.value())
                    .result(billService.getBillBySessionId(sessionId))
                    .build();
        } catch (Exception e) {
            return ApiResponse.<BillResponse>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .result(null)
                    .message(e.getMessage())
                    .build();
        }
    }
}
