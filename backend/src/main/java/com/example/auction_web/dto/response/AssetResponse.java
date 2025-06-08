package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserInfoBase;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.ImageAsset;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssetResponse {
    String assetId;
    String assetName;
    UserInfoBase vendor;
    String slug;
    String mainImage;
    List<ImageAssetResponse> listImages;
    String assetDescription;
    BigDecimal assetPrice;
    TypeResponse type;
    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
