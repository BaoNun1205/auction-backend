package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CategoryResponse {
    String categoryId;
    String categoryName;
    String slug;
    String image;
    Boolean delFlag;
    List<TypeResponse> types;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
