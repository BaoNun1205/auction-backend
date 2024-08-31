package com.example.auction_web.service.auth;

import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.response.auth.UserResponse;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
}
