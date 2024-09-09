package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AssetStatusCreateRequest;
import com.example.auction_web.dto.request.AssetStatusUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AssetStatusResponse;
import com.example.auction_web.service.AssetStatusService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset-status")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AssetStatusController {
    AssetStatusService assetStatusService;

    @PostMapping
    ApiResponse<AssetStatusResponse> createAssetStatus(@RequestBody AssetStatusCreateRequest request) {
        return ApiResponse.<AssetStatusResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetStatusService.createAssetStatus(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AssetStatusResponse> updateAssetStatus(@PathVariable String id, @RequestBody AssetStatusUpdateRequest request) {
        return ApiResponse.<AssetStatusResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetStatusService.updateAssetStatus(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AssetStatusResponse>> getAssetStatuses() {
        return ApiResponse.<List<AssetStatusResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(assetStatusService.getAllAssetStatuses())
                .build();
    }
}
