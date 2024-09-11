package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.ImageAsset;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageAssetMapper {
    @Mapping(target = "asset", ignore = true)
    ImageAsset toImageAsset(ImageAssetCreateRequest request);
    List<ImageAsset> toImageAssetList(List<ImageAssetCreateRequest> requests);

    @Mapping(target = "assetId", source = "asset", qualifiedByName = "assetToString")
    ImageAssetResponse toImageAssetResponse(ImageAsset imageAsset);
    List<ImageAssetResponse> toImageAssetResponseList(List<ImageAsset> imageAssets);
    void updateImageAsset(@MappingTarget ImageAsset imageAsset, ImageAssetUpdateRequest request);

    @Named("assetToString")
    default String assetToString(Asset asset) {
        return asset != null ? asset.getAssetId() : null;
    }
}
