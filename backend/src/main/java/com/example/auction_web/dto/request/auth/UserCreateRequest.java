package com.example.auction_web.dto.request.auth;

import com.example.auction_web.enums.GENDER;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserCreateRequest {
    @NotBlank(message = "Username cannot be blank")
    @Size(min = 4, max = 20, message = "Username must be between 4 and 20 characters")
    String username;
    String name;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, including uppercase, lowercase, number and special character"
    )
    String password;
    String avatar;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    String email;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Phone number is invalid"
    )
    String phone;
    String gender;
    LocalDate dateOfBirth;
    String token;
}
