package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.entity.Asset;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    Asset toAsset(AssetCreateRequest assetCreationRequest);
    AssetResponse toAssetResponse(Asset asset);
    List<AssetResponse> toAssetResponses(List<Asset> assets);
    void updateAsset(@MappingTarget Asset asset, AssetUpdateRequest request);
}
