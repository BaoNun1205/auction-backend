package com.example.auction_web.dto.request;

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
public class AddressUpdateRequest {
    String provinceId;
    String districtId;
    String wardId;
    String addressDetail;
    String phone;
    Boolean delFlag;
    LocalDateTime updatedAt;
}
