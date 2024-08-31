package com.example.auction_web.dto.request.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreateRequest {
    String username;
    String firstName;
    String lastName;
    String password;
    String avatar;
    String email;
    String phone;
    String token;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
