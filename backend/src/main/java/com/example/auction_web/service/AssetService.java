package com.example.auction_web.service;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.enums.ASSET_STATUS;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface AssetService {
    AssetResponse createAsset(AssetCreateRequest request);
    AssetResponse getAssetById(String id);
    AssetResponse updateAsset(String id, AssetUpdateRequest request);
    // AssetResponse updateAssetStatus(String assetId, ASSET_STATUS status);
    List<AssetResponse> filterAssets(String vendorId, String assetName, BigDecimal minPrice, BigDecimal maxPrice,
                                     String insprectorId, String typeId, String status, int page, int size);
    int totalAssets(String vendorId, String assetName, BigDecimal minPrice, BigDecimal maxPrice,
                    String insprectorId, String typeId, String status);
    void deleteAsset(String id);
    void restoreAsset(String id);
}
