package com.example.auction_web.controller;

import com.example.auction_web.dto.request.BillItemCreateRequest;
import com.example.auction_web.dto.request.BillItemUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.BillItemResponse;
import com.example.auction_web.service.BillItemService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bill-item")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class BillItemController {
    BillItemService billItemService;

    @PostMapping
    ApiResponse<BillItemResponse> createBillItem(@RequestBody BillItemCreateRequest request) {
        return ApiResponse.<BillItemResponse>builder()
                .code(HttpStatus.OK.value())
                .result(billItemService.createBillItem(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<BillItemResponse> updateBillItem(@PathVariable String id, @RequestBody BillItemUpdateRequest request) {
        return ApiResponse.<BillItemResponse>builder()
                .code(HttpStatus.OK.value())
                .result(billItemService.updateBillItem(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<BillItemResponse>> getAllBillItems() {
        return ApiResponse.<List<BillItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(billItemService.getAllBillItems())
                .build();
    }

    @GetMapping("/bill/id/{billId}")
    ApiResponse<List<BillItemResponse>> getBillItemsByBillId(@PathVariable String billId) {
        return ApiResponse.<List<BillItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(billItemService.getBillItemsByBillId(billId))
                .build();
    }
}
