package com.example.auction_web.service.auth;

import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.request.auth.UserUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.auth.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UserService {
    UserResponse createUser(UserCreateRequest request);
    UserResponse getUserResponse(String id);
    User getUser(String id);
    UserResponse getUserByUsername(String username);
    void updateAvatar(String erId, MultipartFile image);
    List<UserResponse> getUsers();
    UserResponse getMyInfo();
    UserResponse updateUser(String userId, UserUpdateRequest request);
    void deleteUser(String userId);
}
