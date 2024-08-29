package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.UserCreateRequest;
import com.example.auction_web.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUer(UserCreateRequest request);
}

