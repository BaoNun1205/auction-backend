package com.example.auction_web.dto.request.filter;

import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AssetFilterRequest {
    String vendorId;
    String assetName;
    BigDecimal minPrice;
    BigDecimal maxPrice;
    String insprectorId;
    String typeId;
    String status;
}
