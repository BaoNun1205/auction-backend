package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AssetStatusCreateRequest;
import com.example.auction_web.dto.request.AssetStatusUpdateRequest;
import com.example.auction_web.dto.response.AssetStatusResponse;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.mapper.AssetStatusMapper;
import com.example.auction_web.repository.AssetStatusRepository;
import com.example.auction_web.service.AddressService;
import com.example.auction_web.service.AssetStatusService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AssetStatusServiceImpl implements AssetStatusService {
    AssetStatusRepository assetStatusRepository;
    AssetStatusMapper assetStatusMapper;


    public AssetStatusResponse createAssetStatus(AssetStatusCreateRequest request) {
        return assetStatusMapper.toAssetStatusResponse(assetStatusRepository.save(assetStatusMapper.toAssetStatus(request)));
    }

    public AssetStatusResponse updateAssetStatus(String id, AssetStatusUpdateRequest request) {
        var assetStatus = assetStatusRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ASSET_STATUS_NOT_FOUND));
        assetStatusMapper.updateAssetStatus(assetStatus, request);
        return assetStatusMapper.toAssetStatusResponse(assetStatusRepository.save(assetStatus));
    }

    public List<AssetStatusResponse> getAllAssetStatuses() {
        return assetStatusRepository.findAll().stream()
                .map(assetStatusMapper::toAssetStatusResponse)
                .toList();
    }
}
