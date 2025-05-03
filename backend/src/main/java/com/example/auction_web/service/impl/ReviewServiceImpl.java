package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.ReviewRequest;
import com.example.auction_web.dto.response.ReviewResponse;
import com.example.auction_web.entity.Review;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.ReviewMapper;
import com.example.auction_web.repository.ReviewRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.ReviewService;
import com.example.auction_web.service.auth.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {

    ReviewRepository reviewRepository;
    UserService userService;
    ReviewMapper reviewMapper;
    UserRepository userRepository;

    public ReviewResponse createOrUpdateReview(ReviewRequest request) {
        User reviewer = userService.getUser(request.getReviewerId());
        User reviewee = userService.getUser(request.getRevieweeId());
    
        Optional<Review> existing = reviewRepository.findByReviewerAndRevieweeAndDelFlagFalse(reviewer, reviewee);
    
        double currentAverage = reviewee.getAverageReviewRating() != null ? reviewee.getAverageReviewRating() : 0.0;
        long totalReviews = reviewRepository.countByRevieweeAndDelFlagFalse(reviewee);
    
        Review review;
        if (existing.isPresent()) {
            review = existing.get();
    
            // Tính lại averageReviewRating khi cập nhật review
            int oldRating = review.getRating();
            int newRating = request.getRating();
    
            double newAverage = (currentAverage * totalReviews - oldRating + newRating) / totalReviews;
            reviewee.setAverageReviewRating(newAverage);
    
            review.setRating(newRating);
            review.setComment(request.getComment());
            review.setUpdatedAt(LocalDateTime.now());
        } else {
            review = Review.builder()
                .reviewer(reviewer)
                .reviewee(reviewee)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();
    
            // Tính lại averageReviewRating khi tạo mới review
            double newAverage = (currentAverage * totalReviews + request.getRating()) / (totalReviews + 1);
            reviewee.setAverageReviewRating(newAverage);
        }
    
        reviewRepository.save(review);
        userRepository.save(reviewee);
    
        return reviewMapper.toReviewResponse(review);
    }    

    public List<ReviewResponse> getReviewsOfUser(String revieweeId) {
        User reviewee = userService.getUser(revieweeId);
        return reviewRepository.findByRevieweeAndDelFlagFalse(reviewee)
            .stream()
            .map(reviewMapper::toReviewResponse)
            .toList();
    }

    public List<ReviewResponse> getReviewsByUser(String reviewerId) {
        User reviewer = userService.getUser(reviewerId);
        return reviewRepository.findByReviewerAndDelFlagFalse(reviewer)
            .stream()
            .map(reviewMapper::toReviewResponse)
            .toList();
    }

    public boolean hasUserReviewed(String reviewerId, String revieweeId) {
        User reviewer = userService.getUser(reviewerId);
        User reviewee = userService.getUser(revieweeId);
        return reviewRepository.findByReviewerAndRevieweeAndDelFlagFalse(reviewer, reviewee).isPresent();
    }

    public ReviewResponse getReviewByUser(String reviewerId, String revieweeId) {
        User reviewer = userService.getUser(reviewerId);
        User reviewee = userService.getUser(revieweeId);

        return reviewMapper.toReviewResponse(
                reviewRepository.findByReviewerAndRevieweeAndDelFlagFalse(reviewer, reviewee)
                .orElseThrow(() -> new AppException(ErrorCode.REVIEW_NOT_EXISTED))
        );
    }

    public Long countReviewsByUser(String revieweeId) {
        return reviewRepository.countByRevieweeAndDelFlagFalse(userService.getUser(revieweeId));
    }
}
