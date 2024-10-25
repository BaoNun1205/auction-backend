package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.request.filter.AssetFilterRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.service.AssetService;
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

    @PostMapping
    ApiResponse<AssetResponse> createAsset(@ModelAttribute AssetCreateRequest request){
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

    @GetMapping("/filter")
    ApiResponse<List<AssetResponse>> filterAssets(@RequestParam(required = false) String vendorId,
                                                  @RequestParam(required = false) String assetName,
                                                  @RequestParam(required = false) BigDecimal minPrice,
                                                  @RequestParam(required = false) BigDecimal maxPrice,
                                                  @RequestParam(required = false) String insprectorId,
                                                  @RequestParam(required = false) String typeId,
                                                  @RequestParam(required = false) String status,
                                                  @RequestParam(required = false) Integer page,
                                                  @RequestParam(required = false) Integer size) {
        return ApiResponse.<List<AssetResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(assetService.filterAssets(vendorId, assetName,
                        minPrice, maxPrice, insprectorId,
                        typeId, status, page, size))
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
}
