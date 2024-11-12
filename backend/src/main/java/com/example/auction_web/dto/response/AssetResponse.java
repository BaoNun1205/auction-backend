package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssetResponse {
    String assetId;
    String assetName;
    UserResponse vendor;
    InspectorResponse inspector;
    String slug;
    String mainImage;
    String assetDescription;
    BigDecimal assetPrice;
    TypeResponse type;
    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
