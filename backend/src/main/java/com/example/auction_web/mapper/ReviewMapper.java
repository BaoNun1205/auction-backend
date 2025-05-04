package com.example.auction_web.mapper;

import org.mapstruct.Mapper;

import com.example.auction_web.dto.response.ReviewResponse;
import com.example.auction_web.entity.Review;

@Mapper(componentModel = "spring")
public interface ReviewMapper {
    ReviewResponse toReviewResponse(Review review);
}
