package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.entity.AssetStatus;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.entity.auth.Insprector;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RequirementMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "insprector", ignore = true)
    @Mapping(target = "assetStatus", ignore = true)
    Requirement toRequirement(RequirementCreateRequest request);

    @Mapping(target = "vendorId", source = "user", qualifiedByName = "userToString")
    @Mapping(target = "assetStatusId", source = "assetStatus", qualifiedByName = "assetStatusToString")
    @Mapping(target = "inspectorId", source = "insprector", qualifiedByName = "inspectorToString")
    RequirementResponse toRequirementResponse(Requirement requirement);

    @Mapping(target = "assetStatus", ignore = true)
    void updateRequirement(@MappingTarget Requirement requirement, RequirementUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("assetStatusToString")
    default String assetStatusToString(AssetStatus assetStatus) {
        return assetStatus != null ? assetStatus.getAssetStatusId() : null;
    }

    @Named("inspectorToString")
    default String inspectorToString(Insprector insprector) {
        return insprector != null ? insprector.getInsprectorId() : null;
    }
}
