package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RequirementResponse {
    String requirementId;
    String vendorId;
    String assetName;
    String assetDescription;
    BigDecimal assetPrice;
    String inspectorId;
    String assetStatusId;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
