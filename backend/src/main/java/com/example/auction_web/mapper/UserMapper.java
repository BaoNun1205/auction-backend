package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.auth.UserCreateRequest;
import com.example.auction_web.dto.request.auth.UserUpdateRequest;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUer(UserCreateRequest request);

    @Mapping(source = "userId", target = "userId")
    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

