package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class FavouriteResponse {
    String favouriteId;
    String userId;
    String assetId;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
