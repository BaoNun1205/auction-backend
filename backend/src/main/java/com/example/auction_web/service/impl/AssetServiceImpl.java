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
import com.example.auction_web.service.FileUploadService;
import com.example.auction_web.service.specification.AssetSpecification;
import com.example.auction_web.utils.CreateSlug;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    AssetRepository assetRepository;
    UserRepository userRepository;
    TypeRepository typeRepository;
    FileUploadService fileUploadService;
    AssetMapper assetMapper;


    @PreAuthorize("hasRole('ADMIN')")
    public AssetResponse createAsset(AssetCreateRequest request){
        try {
            var asset = assetMapper.toAsset(request);

            asset.setSlug(CreateSlug.createSlug(asset.getAssetName()));
            asset.setMainImage(fileUploadService.uploadFile(request.getMainImage()));
            setAssetReference(request, asset);

            return assetMapper.toAssetResponse(assetRepository.save(asset));
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AssetResponse updateAsset(String id, AssetUpdateRequest request) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
        assetMapper.updateAsset(asset, request);
        setAssetReference(request, asset);
        return assetMapper.toAssetResponse(assetRepository.save(asset));
    }

    public List<AssetResponse> filterAssets(String vendorId, String assetName, BigDecimal minPrice, BigDecimal maxPrice,
                                            String insprectorId, String typeId, String status) {
        if (isAllParamsNullOrEmpty(vendorId, assetName, minPrice, maxPrice, insprectorId, typeId, status)) {
            return assetRepository.findAll().stream()
                    .map(assetMapper::toAssetResponse)
                    .toList();
        }

        Specification<Asset> specification = Specification
                .where(AssetSpecification.hasVendorId(vendorId))
                .and(AssetSpecification.hasAssetNameContaining(assetName))
                .and(AssetSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(AssetSpecification.hasInsprectorId(insprectorId))
                .and(AssetSpecification.hasTypeId(typeId))
                .and(AssetSpecification.hasStatus(status));

        return assetRepository.findAll(specification).stream()
                .map(assetMapper::toAssetResponse)
                .toList();
    }

    public void deleteAsset(String id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
        asset.setDelFlag(true);
        assetRepository.save(asset);
    }

    public void restoreAsset(String id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
        asset.setDelFlag(false);
        assetRepository.save(asset);
    }

    // setAssetReference method
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

    private boolean isAllParamsNullOrEmpty(String vendorId, String assetName, BigDecimal minPrice, BigDecimal maxPrice,
                                           String inspectorId, String typeId, String status) {
        return (vendorId == null || vendorId.trim().isEmpty()) &&
                (assetName == null || assetName.trim().isEmpty()) &&
                (minPrice == null) &&
                (maxPrice == null) &&
                (inspectorId == null || inspectorId.trim().isEmpty()) &&
                (typeId == null || typeId.trim().isEmpty()) &&
                (status == null || status.trim().isEmpty());
    }
}
