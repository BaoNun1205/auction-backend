package com.example.auction_web.service;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;

import java.util.List;

public interface AssetService {
    AssetResponse createAsset(AssetCreateRequest request);
    AssetResponse updateAsset(String id, AssetUpdateRequest request);
    List<AssetResponse> getAllAssets();
    List<AssetResponse> getAssetByAssetName(String assetName);
}
