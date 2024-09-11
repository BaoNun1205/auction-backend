package com.example.auction_web.service;

import com.example.auction_web.dto.request.AssetStatusCreateRequest;
import com.example.auction_web.dto.request.AssetStatusUpdateRequest;
import com.example.auction_web.dto.response.AssetStatusResponse;

import java.util.List;

public interface AssetStatusService {
    AssetStatusResponse createAssetStatus(AssetStatusCreateRequest request);
    AssetStatusResponse updateAssetStatus(String id, AssetStatusUpdateRequest request);
    List<AssetStatusResponse> getAllAssetStatuses();
}
