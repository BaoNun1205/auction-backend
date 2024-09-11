package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetStatusCreateRequest;
import com.example.auction_web.dto.request.AssetStatusUpdateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.AssetStatusResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AssetStatus;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetStatusMapper {
    AssetStatus toAssetStatus(AssetStatusCreateRequest assetStatusCreateRequest);
    AssetStatusResponse toAssetStatusResponse(AssetStatus assetStatus);
    List<AssetResponse> toAssetStatusResponses(List<AssetStatus> assetStatuses);
    void updateAssetStatus(@MappingTarget AssetStatus assetStatus, AssetStatusUpdateRequest request);
}
