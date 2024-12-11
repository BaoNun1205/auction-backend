package com.example.auction_web.dto.response.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserResponse {
    String userId;
    String username;
    String name;
    String password;
    String avatar;
    String email;
    String phone;
    String gender;
    LocalDate dateOfBirth;
    String token;
    Boolean enabled;
    Set<RoleResponse> roles;
}
