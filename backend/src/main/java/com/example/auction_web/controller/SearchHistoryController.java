package com.example.auction_web.controller;

import com.example.auction_web.dto.request.personalization.SearchHistoryRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.personalization.dto.response.SearchHistoryResponse;
import com.example.auction_web.personalization.service.SearchHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search-history")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class SearchHistoryController {

    SearchHistoryService searchHistoryService;

    // Ghi nhận từ khóa tìm kiếm
    @PostMapping("/record")
    ApiResponse<Void> recordSearch(@RequestBody SearchHistoryRequest request) {
        searchHistoryService.recordSearch(request.getUserId(), request.getKeyword());
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Recorded search keyword successfully")
                .build();
    }

    // Lấy danh sách từ khóa được tìm kiếm nhiều nhất
    @GetMapping("/top")
    ApiResponse<List<SearchHistoryResponse>> getTopKeywords(
            @RequestParam String userId
    ) {
        return ApiResponse.<List<SearchHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(searchHistoryService.getTopKeywords(userId))
                .build();
    }

    // Lấy danh sách từ khóa tìm kiếm gần đây
    @GetMapping("/recent")
    ApiResponse<List<SearchHistoryResponse>> getRecentKeywords(
            @RequestParam String userId
    ) {
        return ApiResponse.<List<SearchHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(searchHistoryService.getRecentKeywords(userId))
                .build();
    }
}
