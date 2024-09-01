package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.ImageAsset;
import com.example.auction_web.mapper.ImageAssetMapper;
import com.example.auction_web.repository.ImageAssetRepository;
import com.example.auction_web.service.ImageAssetService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class ImageAssetServiceImpl implements ImageAssetService {
    ImageAssetRepository imageAssetRepository;
    ImageAssetMapper imageAssetMapper;

    public ImageAssetResponse createImageAsset(ImageAssetCreateRequest request) {
        return imageAssetMapper.toImageAssetResponse(imageAssetRepository.save(imageAssetMapper.toImageAsset(request)));
    }

    public ImageAssetResponse updateImageAsset(String id, ImageAssetUpdateRequest request) {
        ImageAsset imageAsset = imageAssetRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        imageAssetMapper.updateImageAsset(imageAsset, request);
        return imageAssetMapper.toImageAssetResponse(imageAssetRepository.save(imageAsset));
    }

    public List<ImageAssetResponse> findAllImageAssets() {
        return imageAssetRepository.findAll().stream()
                .map(imageAssetMapper::toImageAssetResponse)
                .toList();
    }

    public List<ImageAssetResponse> findImageAssetByAsset(Asset asset) {
        return imageAssetRepository.findImageAssetByAsset(asset).stream()
                .map(imageAssetMapper::toImageAssetResponse)
                .toList();
    }
}
