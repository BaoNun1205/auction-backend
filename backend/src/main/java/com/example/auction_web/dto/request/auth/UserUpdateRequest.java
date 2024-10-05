package com.example.auction_web.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String firstName;
    String lastName;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, including uppercase, lowercase, number and special character"
    )
    String password;
    String avatar;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Phone number is invalid"
    )
    String phone;
    List<String> roles;
}
