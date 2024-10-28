package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RequirementMapper {
    @Mapping(target = "vendor", ignore = true)
    Requirement toRequirement(RequirementCreateRequest request);

    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "userToString")
    @Mapping(target = "inspector", source = "inspector", qualifiedByName = "userToString")
    RequirementResponse toRequirementResponse(Requirement requirement);

    void updateRequirement(@MappingTarget Requirement requirement, RequirementUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUsername() : null;
    }

//    @Named("imageToString")
//    default List imageToString(List<ImageRequirement> imageRequirements) {
//        if (imageRequirements == null || imageRequirements.isEmpty()) {
//            return "";
//        }
//
//        return imageRequirements.stream()
//                .map(ImageRequirement::getImage));
//    }

    @Named("inspectorToString")
    default String inspectorToString(Inspector inspector) {
        return inspector != null ? inspector.getUser().getUsername() : null;
    }
}
