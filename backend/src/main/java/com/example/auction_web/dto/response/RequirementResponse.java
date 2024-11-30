package com.example.auction_web.dto.response;

import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.ImageRequirement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RequirementResponse {
    String requirementId;
    UserResponse vendor;
    String assetName;
    String assetDescription;
    BigDecimal assetPrice;
    UserResponse inspector;
    String assetStatusId;
    String status;
    Boolean delFlag;
    List<ImageRequirementResponse> imageRequirements;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
