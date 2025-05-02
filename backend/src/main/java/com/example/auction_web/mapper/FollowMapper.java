package com.example.auction_web.mapper;

import com.example.auction_web.dto.response.FollowResponse;
import com.example.auction_web.entity.Follow;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FollowMapper {
    FollowResponse toFollowResponse(Follow follow);
}

