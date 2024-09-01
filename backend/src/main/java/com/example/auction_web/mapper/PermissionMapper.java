package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.auth.PermissionRequest;
import com.example.auction_web.dto.response.auth.PermissionResponse;
import com.example.auction_web.entity.auth.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
