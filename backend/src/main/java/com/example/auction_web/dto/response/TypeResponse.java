package com.example.auction_web.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class TypeResponse {
    String typeId;
    String categoryId;
    String categoryName;
    String typeName;
    String slug;
    String image;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
