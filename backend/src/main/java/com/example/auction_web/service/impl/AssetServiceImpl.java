package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Type;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.TypeRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AssetService;
import com.example.auction_web.utils.CreateSlug;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    AssetRepository assetRepository;
    UserRepository userRepository;
    TypeRepository typeRepository;
    AssetMapper assetMapper;


    public AssetResponse createAsset(AssetCreateRequest request) {
        var asset = assetMapper.toAsset(request);
        asset.setSlug(CreateSlug.createSlug(asset.getAssetName()));
        setAssetReference(request, asset);
        return assetMapper.toAssetResponse(assetRepository.save(asset));
    }

    public AssetResponse updateAsset(String id, AssetUpdateRequest request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
        assetMapper.updateAsset(asset, request);
        setAssetReference(request, asset);
        return assetMapper.toAssetResponse(assetRepository.save(asset));
    }

    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll().stream()
                .map(assetMapper::toAssetResponse)
                .toList();
    }

    public List<AssetResponse> getAssetByAssetName(String assetName) {
        if (!assetRepository.existsByAssetNameContaining(assetName)) {
            throw new AppException(ErrorCode.ASSET_NOT_EXISTED);
        }
        return assetRepository.findByAssetNameContaining(assetName).stream()
                .map(assetMapper::toAssetResponse)
                .toList();
    }

    private void setAssetReference(Object request, Asset asset) {
        if (request instanceof AssetCreateRequest createRequest) {
            asset.setUser(getUserById(createRequest.getVendorId()));
            asset.setType(getTypeById(createRequest.getTypeId()));
        } else if (request instanceof AssetUpdateRequest updateRequest) {
            asset.setType(getTypeById(updateRequest.getTypeId()));
        }
    }

    private User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    private Type getTypeById(String typeId) {
        return typeRepository.findById(typeId)
                .orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED));
    }
}
