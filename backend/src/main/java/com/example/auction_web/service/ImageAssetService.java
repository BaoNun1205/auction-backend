package com.example.auction_web.service;

import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.ImageAsset;

import java.util.List;

public interface ImageAssetService {
    ImageAssetResponse createImageAsset(ImageAssetCreateRequest request);
    ImageAssetResponse updateImageAsset(String Id, ImageAssetUpdateRequest request);
    List<ImageAssetResponse> findAllImageAssets();
    List<ImageAssetResponse> findImageAssetByAssetId(String assetId);
}
