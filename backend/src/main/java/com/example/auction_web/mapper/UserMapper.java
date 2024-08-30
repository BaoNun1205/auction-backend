package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.UserCreateRequest;
import com.example.auction_web.dto.request.UserUpdateRequest;
import com.example.auction_web.dto.response.UserResponse;
import com.example.auction_web.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUer(UserCreateRequest request);

    UserResponse toUserResponse(User user);

//    @Mapping(target = "roles", ignore = true)
//    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}

