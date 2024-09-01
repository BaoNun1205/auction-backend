package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.auth.RoleRequest;
import com.example.auction_web.dto.response.auth.RoleResponse;
import com.example.auction_web.entity.auth.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
