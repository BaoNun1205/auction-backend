package com.example.auction_web.controller;

import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.request.auth.UserUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.dto.response.statistical.UserCountResponse;
import com.example.auction_web.service.auth.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request){
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Create auction item success")
                .result(userService.createUser(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .result(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserResponse(userId))
                .build();
    }

    @GetMapping("/username/{username}")
    ApiResponse<UserResponse> getUserByUsername(@PathVariable("username") String username) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserByUsername(username))
                .build();
    }

//    @GetMapping("/{username}")
//    ApiResponse<UserResponse> getUser(@PathVariable("username") String username) {
//        return ApiResponse.<UserResponse>builder()
//                .result(userService.getUserByUsername(username))
//                .build();
//    }

    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PostMapping("/update-avatar/{userId}")
    ApiResponse<String> updateAvatar(
            @PathVariable String userId,
            @RequestParam("avatar") MultipartFile avatarFile) {
        userService.updateAvatar(userId, avatarFile);
        return ApiResponse.<String>builder()
                .result("Avatar updated successfully")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> update(@PathVariable String userId, @ModelAttribute UserUpdateRequest request){
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(userId, request))
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<String> delete(@PathVariable String userId){
        userService.deleteUser(userId);
        return ApiResponse.<String>builder().result("User has been deleted").build();
    }

    @PutMapping("/notifications/unread/{userId}")
    ApiResponse<String> updateUnreadNotificationCount(
            @PathVariable String userId,
            @RequestParam("count") Long count) {
        userService.updateUnreadNotificationCount(userId, count);
        return ApiResponse.<String>builder()
                .result("Unread notification count updated successfully")
                .build();
    }

    @GetMapping("/active/count")
    ApiResponse<UserCountResponse> countActiveUsers() {
        long totalCount = userService.countActiveUsers();
        int currentYear = java.time.Year.now().getValue();
        long countOfYear = userService.countActiveUsersByYear(currentYear);
        double growthRate = userService.getUserGrowthRateThisYear();
        UserCountResponse response = new UserCountResponse(totalCount, countOfYear, growthRate);
        return ApiResponse.<UserCountResponse>builder()
                .result(response)
                .build();
    }
}
