package com.example.auction_web.service;

import com.example.auction_web.dto.request.CategoryCreateRequest;
import com.example.auction_web.dto.request.CategoryUpdateRequest;
import com.example.auction_web.dto.response.CategoryResponse;
import lombok.experimental.FieldDefaults;

import java.util.List;

//@FieldDefaults(level = lombok.AccessLevel.PUBLIC)
public interface CategoryService {
    CategoryResponse getCategory(String id);
    List<CategoryResponse> getAllCategories();
    CategoryResponse createCategory(CategoryCreateRequest request);
    CategoryResponse updateCategory(String id, CategoryUpdateRequest request);
    void deleteCategory(String id);
}
