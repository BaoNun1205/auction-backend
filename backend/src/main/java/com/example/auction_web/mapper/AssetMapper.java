package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AssetCreateRequest;
import com.example.auction_web.dto.request.AssetUpdateRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Type;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AssetMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "type", ignore = true)
    Asset toAsset(AssetCreateRequest assetCreationRequest);

    @Mapping(source = "user", target = "vendorId", qualifiedByName = "userToString")
    @Mapping(source = "type", target = "typeId", qualifiedByName = "typeToString")
    AssetResponse toAssetResponse(Asset asset);
    List<AssetResponse> toAssetResponses(List<Asset> assets);

    @Mapping(target = "type", ignore = true)
    void updateAsset(@MappingTarget Asset asset, AssetUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("typeToString")
    default String typeToString(Type type) {
        return type != null ? type.getTypeId() : null;
    }
}
