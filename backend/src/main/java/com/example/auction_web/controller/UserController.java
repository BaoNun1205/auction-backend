package com.example.auction_web.controller;

import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.service.auth.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
