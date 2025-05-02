package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FollowResponse {
    String followId;
    UserResponse follower;
    UserResponse followee;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

