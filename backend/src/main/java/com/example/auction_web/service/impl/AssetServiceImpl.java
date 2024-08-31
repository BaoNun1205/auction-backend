package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.service.AssetService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    AssetRepository assetRepository;
    AssetMapper assetMapper;


    public AssetResponse createAsset(AssetCreateRequest request) {
        return assetMapper.toAssetResponse(assetRepository.save(assetMapper.toAsset(request)));
    }


    public AssetResponse updateAsset(String id, AssetUpdateRequest request) {
        Asset asset = assetRepository.findById(id).orElseThrow();
        assetMapper.updateAsset(asset, request);
        return assetMapper.toAssetResponse(assetRepository.save(asset));
    }


    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::toAssetResponse)
                .toList();
    }

    public List<AssetResponse> getAssetByAssetName(String assetName) {
        List<Asset> assets = assetRepository.findByAssetNameContaining(assetName);
        return assetMapper.toAssetResponses(assets);

    }
}
