package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
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

    @Mapping(target = "vendor", source = "vendor", qualifiedByName = "userToResponse")
    @Mapping(target = "inspector", source = "inspector", qualifiedByName = "userToResponse")
    RequirementResponse toRequirementResponse(Requirement requirement);

    void updateRequirement(@MappingTarget Requirement requirement, RequirementUpdateRequest request);

    @Named("userToResponse")
    default UserResponse userToResponse(User user) {
        if (user == null) return null;
        return UserResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .username(user.getUsername())
                .build();
    }

}
