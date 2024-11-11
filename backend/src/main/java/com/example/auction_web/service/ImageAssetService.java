package com.example.auction_web.service;

import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.Asset;

import java.util.List;

public interface ImageAssetService {
    void createImageAsset(List<String> images,Asset asset);
    List<ImageAssetResponse> findAllImageAssets();
    List<ImageAssetResponse> findImageAssetByAssetId(String assetId);
    List<ImageAssetResponse> findAllImageAssetsByAssetId(String assetId);
}
