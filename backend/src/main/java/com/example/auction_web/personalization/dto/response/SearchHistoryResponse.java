package com.example.auction_web.personalization.dto.response;

import java.time.LocalDateTime;

import com.example.auction_web.dto.response.auth.UserResponse;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchHistoryResponse {
    String searchId;
    UserResponse user;
    String keyword;
    int searchCount;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}

