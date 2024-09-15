package com.example.auction_web.controller;

import com.example.auction_web.dto.request.FavouriteCreateRequest;
import com.example.auction_web.dto.request.FavouriteUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.FavouriteResponse;
import com.example.auction_web.service.FavouriteService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/favourite")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class FavouriteController {
    FavouriteService favouriteService;

    @PostMapping
    ApiResponse<FavouriteResponse> createFavourite(@RequestBody FavouriteCreateRequest request) {
        return ApiResponse.<FavouriteResponse>builder()
                .code(HttpStatus.OK.value())
                .result(favouriteService.createFavourite(request))
                .build();
    }

    @PutMapping("/{favouriteId}")
    ApiResponse<FavouriteResponse> updateFavourite(@PathVariable String favouriteId, @RequestBody FavouriteUpdateRequest request) {
        return ApiResponse.<FavouriteResponse>builder()
                .code(HttpStatus.OK.value())
                .result(favouriteService.updateFavourite(favouriteId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<FavouriteResponse>> getAllFavourites() {
        return ApiResponse.<List<FavouriteResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(favouriteService.getAllFavourites())
                .build();
    }

    @GetMapping("/user/id/{userId}")
    ApiResponse<List<FavouriteResponse>> getFavouritesByUserId(@PathVariable String userId) {
        return ApiResponse.<List<FavouriteResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(favouriteService.getFavouritesByUserId(userId))
                .build();
    }

    @GetMapping("/asset/id/{assetId}")
    ApiResponse<List<FavouriteResponse>> getFavouritesByAssetId(@PathVariable String assetId) {
        return ApiResponse.<List<FavouriteResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(favouriteService.getFavouritesByAssetId(assetId))
                .build();
    }
}
