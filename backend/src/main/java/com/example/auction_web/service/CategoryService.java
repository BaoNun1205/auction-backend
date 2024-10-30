package com.example.auction_web.service;

import com.example.auction_web.dto.request.CategoryCreateRequest;
import com.example.auction_web.dto.request.CategoryUpdateRequest;
import com.example.auction_web.dto.response.CategoryResponse;
import lombok.experimental.FieldDefaults;

import java.util.List;

//@FieldDefaults(level = lombok.AccessLevel.PUBLIC)
public interface CategoryService {
    List<CategoryResponse> filterCategories(Boolean status, String keyword, int page, int size);
    int totalCategories(Boolean status, String keyword);
    CategoryResponse getCategory(String id);
    CategoryResponse createCategory(CategoryCreateRequest request);
    CategoryResponse updateCategory(String id, CategoryUpdateRequest request);
    void deleteCategory(String id);
    void restoreCategory(String id);
}
