package com.example.auction_web.dto.response;

import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    String reviewId;
    UserResponse reviewer;
    UserResponse reviewee;
    int rating;
    String comment;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
