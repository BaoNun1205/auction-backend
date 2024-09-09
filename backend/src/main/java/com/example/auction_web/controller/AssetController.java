package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AssetController {
    AssetService assetService;

    @PostMapping
    ApiResponse<AssetResponse> createAsset(@RequestBody AssetCreateRequest request) {
        return ApiResponse.<AssetResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.createAsset(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AssetResponse> updateAsset(@PathVariable String id, @RequestBody AssetUpdateRequest request) {
        return ApiResponse.<AssetResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.updateAsset(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AssetResponse>> getAssets() {
        return ApiResponse.<List<AssetResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.getAllAssets())
                .build();
    }

    @GetMapping("/asset/name/{assetName}")
    ApiResponse<List<AssetResponse>> getAssetByAssetName(@PathVariable String assetName) {
        return ApiResponse.<List<AssetResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.getAssetByAssetName(assetName))
                .build();
    }
}
