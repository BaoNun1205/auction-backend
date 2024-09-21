package com.example.auction_web.dto.request;

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
public class RequirementCreateRequest {
    String vendorId;
    String assetName;
    String assetDescription;
    BigDecimal assetPrice;
    String inspectorId;
    String assetStatusId;
    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
