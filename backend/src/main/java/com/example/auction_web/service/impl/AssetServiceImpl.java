package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.InspectorResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.entity.Type;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.mapper.TypeMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.InspectorRepository;
import com.example.auction_web.repository.RequirementRepository;
import com.example.auction_web.repository.TypeRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.*;
import com.example.auction_web.service.specification.AssetSpecification;
import com.example.auction_web.utils.CreateSlug;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {
    AssetRepository assetRepository;
    UserRepository userRepository;
    TypeRepository typeRepository;
    ImageAssetService imageAssetService;
    AssetMapper assetMapper;
    TypeMapper typeMapper;
    RequirementService requirementService;
    InspectorService inspectorService;
    InspectorRepository inspectorRepository;

    @PreAuthorize("hasRole('ADMIN')")
    public AssetResponse createAsset(AssetCreateRequest request){
        try {
            var asset = assetMapper.toAsset(request);

            asset.setSlug(CreateSlug.createSlug(asset.getAssetName()));
            asset.setMainImage(request.getImages().getFirst());
            // Set user (vendor)
            User vendor = userRepository.findById(request.getVendorId())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
            asset.setVendor(vendor);

            // Set inspector
            Inspector inspector = inspectorRepository.findInspectorByUser_UserId(request.getInspectorId());
            asset.setInspector(inspector);

            // Set type
            Type type = typeRepository.findById(request.getTypeId())
                    .orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED));
            asset.setType(type);

            // Set requirement and update its status
            Requirement requirement = requirementService.getRequirementById(request.getRequirementId());
            requirement.setDelFlag(true);
            asset.setRequirement(requirement);

            Asset newAsset = assetRepository.save(asset);

            imageAssetService.createImageAsset(request.getImages(), newAsset);

            return assetMapper.toAssetResponse(newAsset);
        } catch (Exception e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public AssetResponse getAssetById(String id) {
        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
        AssetResponse assetResponse = assetMapper.toAssetResponse(asset);
        assetResponse.setType(typeMapper.toTypeResponse(typeRepository.findById(asset.getType().getTypeId()).get()));
        return assetResponse;
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
                                            String inspectorId, String typeId, String status, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (isAllParamsNullOrEmpty(vendorId, assetName, minPrice, maxPrice, inspectorId, typeId, status)) {
            return assetRepository.findAll().stream()
                    .map(assetMapper::toAssetResponse)
                    .toList();
        }

        Specification<Asset> specification = Specification
                .where(AssetSpecification.hasVendorId(vendorId))
                .and(AssetSpecification.hasAssetNameContaining(assetName))
                .and(AssetSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(AssetSpecification.hasInspectorId(inspectorId))
                .and(AssetSpecification.hasTypeId(typeId))
                .and(AssetSpecification.hasStatus(status));

        return assetRepository.findAll(specification, pageable).stream()
                .map(assetMapper::toAssetResponse)
                .toList();
    }

    public int totalAssets(String vendorId, String assetName, BigDecimal minPrice, BigDecimal maxPrice,
                           String inspectorId, String typeId, String status) {
        if (isAllParamsNullOrEmpty(vendorId, assetName, minPrice, maxPrice, inspectorId, typeId, status)) {
            return assetRepository.findAll().size();
        }

        Specification<Asset> specification = Specification
                .where(AssetSpecification.hasVendorId(vendorId))
                .and(AssetSpecification.hasAssetNameContaining(assetName))
                .and(AssetSpecification.hasPriceBetween(minPrice, maxPrice))
                .and(AssetSpecification.hasInspectorId(inspectorId))
                .and(AssetSpecification.hasTypeId(typeId))
                .and(AssetSpecification.hasStatus(status));

        return assetRepository.findAll(specification).size();
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
            asset.setVendor(getUserById(createRequest.getVendorId()));
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
