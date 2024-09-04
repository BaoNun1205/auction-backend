package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.CategoryCreateRequest;
import com.example.auction_web.dto.request.CategoryUpdateRequest;
import com.example.auction_web.dto.response.CategoryResponse;
import com.example.auction_web.entity.Category;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.CategoryMapper;
import com.example.auction_web.repository.CategoryRepository;
import com.example.auction_web.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;

    public CategoryResponse createCategory(CategoryCreateRequest request) {
        return categoryMapper.toCategoryResponse(categoryRepository.save(categoryMapper.toCategory(request)));
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryMapper.updateCategory(category, request);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }
}
