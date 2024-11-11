package com.example.auction_web.service.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.ImageAsset;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.ImageAssetMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.ImageAssetRepository;
import com.example.auction_web.service.ImageAssetService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ImageAssetServiceImpl implements ImageAssetService {
    ImageAssetRepository imageAssetRepository;
    AssetRepository assetRepository;
    ImageAssetMapper imageAssetMapper;

    public void createImageAsset(List<String> images, Asset asset) {
        List<ImageAsset> imageAssetList = new ArrayList<>();
        for (String image : images) {
            ImageAsset imageAsset = new ImageAsset();
            imageAsset.setAsset(asset);
            imageAsset.setImageAsset(image);
            imageAssetList.add(imageAsset);
        }
        imageAssetRepository.saveAll(imageAssetList);
    }

    public List<ImageAssetResponse> findAllImageAssets() {
        return imageAssetRepository.findAll().stream()
                .map(imageAssetMapper::toImageAssetResponse)
                .toList();
    }

    public List<ImageAssetResponse> findAllImageAssetsByAssetId(String assetId) {
        return imageAssetRepository.findImageAssetsByAsset_AssetId(assetId).stream()
                .map(imageAssetMapper::toImageAssetResponse)
                .toList();
    }

    public List<ImageAssetResponse> findImageAssetByAssetId(String assetId) {
        if (!assetRepository.existsById(assetId)) {
            throw new AppException(ErrorCode.ASSET_NOT_EXISTED);
        }
        return imageAssetRepository.findImageAssetsByAsset_AssetId(assetId).stream()
                .map(imageAssetMapper::toImageAssetResponse)
                .toList();
    }

    void setAssetReference(ImageAsset imageAsset, ImageAssetCreateRequest request) {
        imageAsset.setAsset(getAssetById(request.getAssetId()));
    }

    Asset getAssetById(String assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
    }
}
