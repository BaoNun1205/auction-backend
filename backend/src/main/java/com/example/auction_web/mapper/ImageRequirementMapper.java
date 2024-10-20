package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.ImageRequirementCreateRequest;
import com.example.auction_web.dto.request.ImageRequirementUpdateRequest;
import com.example.auction_web.dto.response.ImageRequirementResponse;
import com.example.auction_web.entity.ImageRequirement;
import com.example.auction_web.entity.Requirement;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ImageRequirementMapper {

    @Mapping(target = "image", ignore = true)
    @Mapping(target = "requirement", ignore = true)
    ImageRequirement toImageRequirement(ImageRequirementCreateRequest request);

    @Mapping(target = "requirementId", source = "requirement", qualifiedByName = "requirementToString")
    ImageRequirementResponse toImageRequirementResponse(ImageRequirement imageRequirement);

    void updateImageRequirement(@MappingTarget ImageRequirement imageRequirement, ImageRequirementUpdateRequest request);

    @Named("requirementToString")
    default String requirementToString(Requirement requirement) {
        return requirement != null ? requirement.getRequirementId() : null;
    }
}
