package com.example.auction_web.repository;

import com.example.auction_web.entity.Review;
import com.example.auction_web.entity.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, String> {
    List<Review> findByRevieweeAndDelFlagFalse(User reviewee);
    Optional<Review> findByReviewerAndRevieweeAndDelFlagFalse(User reviewer, User reviewee);
    List<Review> findByReviewerAndDelFlagFalse(User reviewer);
    Long countByRevieweeAndDelFlagFalse(User reviewee);
}
