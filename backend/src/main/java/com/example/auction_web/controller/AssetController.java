package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.request.filter.AssetFilterRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.DataResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.service.AssetService;
import com.example.auction_web.service.SessionWinnerService;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/asset")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AssetController {
    AssetService assetService;
    SessionWinnerService sessionWinnerService;

    @PostMapping
    ApiResponse<AssetResponse> createAsset(@ModelAttribute AssetCreateRequest request){
        return ApiResponse.<AssetResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.createAsset(request))
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<AssetResponse> getAssetById(@PathVariable String id) {
        return ApiResponse.<AssetResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.getAssetById(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AssetResponse> updateAsset(@PathVariable String id, @RequestBody AssetUpdateRequest request) {
        return ApiResponse.<AssetResponse>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.updateAsset(id, request))
                .build();
    }

    @GetMapping()
    ApiResponse<DataResponse> filterAssets(@RequestParam(required = false) String vendorId,
                                           @RequestParam(required = false) String assetName,
                                           @RequestParam(required = false) BigDecimal minPrice,
                                           @RequestParam(required = false) BigDecimal maxPrice,
                                           @RequestParam(required = false) String inspectorId,
                                           @RequestParam(required = false) String typeId,
                                           @RequestParam(required = false) String status,
                                           @RequestParam(required = false, defaultValue = "0") Integer page,
                                           @RequestParam(required = false, defaultValue = "20") Integer size) {
        List<AssetResponse> filteredAssets = assetService.filterAssets(vendorId, assetName, minPrice, maxPrice, inspectorId, typeId, status, page, size);
        int total = assetService.totalAssets(vendorId, assetName, minPrice, maxPrice, inspectorId, typeId, status);
        return ApiResponse.<DataResponse>builder()
                .code(HttpStatus.OK.value())
                .result(DataResponse.<List<AssetResponse>>builder()
                        .data(filteredAssets)
                        .total(total)
                        .build())
                .build();
    }

    @PutMapping("/restore/{assetId}")
    ApiResponse<String> restoreAsset(@PathVariable String assetId) {
        assetService.restoreAsset(assetId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Asset restored successfully")
                .build();
    }

    @DeleteMapping("/{assetId}")
    ApiResponse<String> deleteAsset(@PathVariable String assetId) {
        assetService.deleteAsset(assetId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Asset deleted successfully")
                .build();
    }

    
    @PutMapping("/status/{assetId}")
    ApiResponse<AssetResponse> updateAssetStatus(@PathVariable String assetId,
                                                 @RequestParam ASSET_STATUS status) {
        return ApiResponse.<AssetResponse>builder()
                .code(HttpStatus.OK.value())
                .result(sessionWinnerService.updateAssetStatus(assetId, status))
                .build();
    }
}
