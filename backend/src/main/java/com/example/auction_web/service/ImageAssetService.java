package com.example.auction_web.service;

import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.ImageAsset;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageAssetService {
    List<ImageAssetResponse> findAllImageAssets();
    List<ImageAssetResponse> findImageAssetByAssetId(String assetId);
}
