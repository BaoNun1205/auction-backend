package com.example.auction_web.controller;

import com.example.auction_web.dto.request.FollowRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.FollowResponse;
import com.example.auction_web.service.FollowService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FollowController {

    FollowService followService;

    // Follow người dùng
    @PostMapping
    ApiResponse<FollowResponse> followUser(@RequestBody FollowRequest request) {
        return ApiResponse.<FollowResponse>builder()
                .code(HttpStatus.OK.value())
                .result(followService.followUser(request.getFollowerId(), request.getFolloweeId()))
                .build();
    }

    // Unfollow người dùng
    @PutMapping("/unfollow")
    ApiResponse<Void> unfollowUser(@RequestBody FollowRequest request) {
        followService.unfollowUser(request.getFollowerId(), request.getFolloweeId());
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .result(null)
                .build();
    }

    // Kiểm tra có đang follow không
    @GetMapping("/check")
    ApiResponse<Boolean> isFollowing(@RequestParam String followerId, @RequestParam String followeeId) {
        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(followService.isFollowing(followerId, followeeId))
                .build();
    }

    // Lấy danh sách đang follow (followee) của người dùng
    @GetMapping("/following/{followerId}")
    ApiResponse<List<FollowResponse>> getFollowing(@PathVariable String followerId) {
        return ApiResponse.<List<FollowResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(followService.getFollowing(followerId))
                .build();
    }

    // Lấy danh sách người đang theo dõi user (follower)
    @GetMapping("/followers/{followeeId}")
    ApiResponse<List<FollowResponse>> getFollowers(@PathVariable String followeeId) {
        return ApiResponse.<List<FollowResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(followService.getFollowers(followeeId))
                .build();
    }

    // Đếm số lượng follower
    @GetMapping("/followers/count/{followeeId}")
    ApiResponse<Long> countFollowers(@PathVariable String followeeId) {
        return ApiResponse.<Long>builder()
                .code(HttpStatus.OK.value())
                .result(followService.countFollowers(followeeId))
                .build();
    }
}
