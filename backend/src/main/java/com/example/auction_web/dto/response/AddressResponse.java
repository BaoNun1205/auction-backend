package com.example.auction_web.dto.response;

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
    String userId;
    String recipientName;
    String provinceId;
    String districtId;
    String wardId;
    String addressDetail;
    String phone;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
