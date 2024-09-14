package com.example.auction_web.controller;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.BillResponse;
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

    @GetMapping("/deposit/id/{depositId}")
    ApiResponse<BillResponse> findBillByDeposit_DepositId(@PathVariable String depositId) {
        return ApiResponse.<BillResponse>builder()
                .code(HttpStatus.OK.value())
                .result(billService.findBillByDeposit_DepositId(depositId))
                .build();
    }
}
