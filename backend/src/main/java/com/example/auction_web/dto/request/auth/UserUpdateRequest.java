package com.example.auction_web.dto.request.auth;

import com.example.auction_web.enums.GENDER;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class UserUpdateRequest {
    String name;

    @NotBlank(message = "Password cannot be blank")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must be at least 8 characters, including uppercase, lowercase, number and special character"
    )
    String password;
    MultipartFile avatar;

    @NotBlank(message = "Phone cannot be blank")
    @Pattern(
            regexp = "^\\+?[0-9]{7,15}$",
            message = "Phone number is invalid"
    )
    String phone;
    String email;
    String gender;
    LocalDate dateOfBirth;
    List<String> roles;
}
