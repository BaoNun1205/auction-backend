package com.example.auction_web.service;

import java.util.List;

import com.example.auction_web.dto.request.ReviewRequest;
import com.example.auction_web.dto.response.ReviewResponse;

public interface ReviewService {
    ReviewResponse createOrUpdateReview(ReviewRequest request);
    List<ReviewResponse> getReviewsOfUser(String revieweeId);
    List<ReviewResponse> getReviewsByUser(String reviewerId);
    boolean hasUserReviewed(String reviewerId, String revieweeId);
    ReviewResponse getReviewByUser(String reviewerId, String revieweeId);
    Long countReviewsByUser(String revieweeId);
}
