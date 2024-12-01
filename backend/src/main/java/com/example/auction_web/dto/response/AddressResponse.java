package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.auth.User;
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
public class AddressResponse {
    String addressId;
    UserResponse user;
    String recipientName;
    String province;
    String district;
    String ward;
    String addressDetail;
    String phone;
    Boolean isDefault;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
