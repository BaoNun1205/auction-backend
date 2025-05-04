package com.example.auction_web.controller;

import com.example.auction_web.dto.request.ReviewRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.ReviewResponse;
import com.example.auction_web.service.ReviewService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReviewController {

    ReviewService reviewService;

    // Tạo hoặc cập nhật đánh giá
    @PostMapping
    ApiResponse<ReviewResponse> createOrUpdateReview(@RequestBody ReviewRequest request) {
        return ApiResponse.<ReviewResponse>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.createOrUpdateReview(request))
                .build();
    }

    // Kiểm tra đã đánh giá hay chưa
    @GetMapping("/check")
    ApiResponse<Boolean> hasUserReviewed(
            @RequestParam String reviewerId,
            @RequestParam String revieweeId
    ) {
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.hasUserReviewed(reviewerId, revieweeId))
                .build();
    }

    // Lấy danh sách đánh giá của người dùng (người được đánh giá)
    @GetMapping("/reviewee/{revieweeId}")
    ApiResponse<List<ReviewResponse>> getReviewsOfUser(@PathVariable String revieweeId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.getReviewsOfUser(revieweeId))
                .build();
    }

    // Lấy danh sách đánh giá mà người dùng đã tạo (người đánh giá)
    @GetMapping("/reviewer/{reviewerId}")
    ApiResponse<List<ReviewResponse>> getReviewsByUser(@PathVariable String reviewerId) {
        return ApiResponse.<List<ReviewResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.getReviewsByUser(reviewerId))
                .build();
    }

    @GetMapping
    public ApiResponse<ReviewResponse> getReviewByUser(
        @RequestParam String reviewerId,
        @RequestParam String revieweeId
    ) {
        return ApiResponse.<ReviewResponse>builder()
            .code(HttpStatus.OK.value())
            .result(reviewService.getReviewByUser(reviewerId, revieweeId))
            .build();
    }
    
    // Đếm số lượng đánh giá của người dùng
    @GetMapping("/count/{revieweeId}")
    ApiResponse<Long> countReviewsByUser(@PathVariable String revieweeId) {
        return ApiResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .result(reviewService.countReviewsByUser(revieweeId))
                .build();
    }
}
