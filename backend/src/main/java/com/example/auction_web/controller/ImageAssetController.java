package com.example.auction_web.controller;

import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.service.ImageAssetService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image-asset")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ImageAssetController {
    ImageAssetService imageAssetService;

    @GetMapping
    ApiResponse<List<ImageAssetResponse>> getAllImageAssets() {
        return ApiResponse.<List<ImageAssetResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(imageAssetService.findAllImageAssets())
                .build();
    }

    @GetMapping("/asset/id/{assetId}")
    ApiResponse<List<ImageAssetResponse>> getImageAssetsByAssetId(@PathVariable String assetId) {
        return ApiResponse.<List<ImageAssetResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(imageAssetService.findImageAssetByAssetId(assetId))
                .build();
    }
}
