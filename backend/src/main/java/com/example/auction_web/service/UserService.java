package com.example.auction_web.service;

import com.example.auction_web.dto.request.UserCreateRequest;
import com.example.auction_web.dto.response.UserResponse;
import com.example.auction_web.entity.User;

public interface UserService {
    public User createUser(UserCreateRequest request);
}
