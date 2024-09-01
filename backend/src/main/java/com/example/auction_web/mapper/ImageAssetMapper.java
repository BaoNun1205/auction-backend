package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.ImageAssetCreateRequest;
import com.example.auction_web.dto.request.ImageAssetUpdateRequest;
import com.example.auction_web.dto.response.ImageAssetResponse;
import com.example.auction_web.entity.ImageAsset;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageAssetMapper {
    ImageAsset toImageAsset(ImageAssetCreateRequest request);
    List<ImageAsset> toImageAssetList(List<ImageAssetCreateRequest> requests);
    ImageAssetResponse toImageAssetResponse(ImageAsset imageAsset);
    List<ImageAssetResponse> toImageAssetResponseList(List<ImageAsset> imageAssets);
    void updateImageAsset(@MappingTarget ImageAsset imageAsset, ImageAssetUpdateRequest request);
}
