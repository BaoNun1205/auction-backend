package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.CategoryCreateRequest;
import com.example.auction_web.dto.request.CategoryUpdateRequest;
import com.example.auction_web.dto.response.CategoryResponse;
import com.example.auction_web.entity.Category;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toCategory(CategoryCreateRequest request);
    CategoryResponse toCategoryCreateResponse(Category category);
    List<CategoryResponse> toCategoryCreateResponses(List<Category> categories);
    void updateCategory(Category category, CategoryUpdateRequest request);
}
