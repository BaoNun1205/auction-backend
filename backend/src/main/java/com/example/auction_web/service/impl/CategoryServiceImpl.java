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
import com.example.auction_web.service.FileUploadService;
import com.example.auction_web.utils.CreateSlug;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    FileUploadService fileUploadService;

    public CategoryResponse getCategory(String id) {
        return categoryMapper.toCategoryResponse(categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED))
        );
    }

    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> filterCategories(Boolean status, String keyword) {
        if (status == null && (keyword == null || keyword.isEmpty())) {
            return categoryRepository.findAll().stream()
                    .map(categoryMapper::toCategoryResponse)
                    .toList();
        }

        // Nếu chỉ có status
        if (status != null && (keyword == null || keyword.isEmpty())) {
            return categoryRepository.findByDelFlag(status).stream()
                    .map(categoryMapper::toCategoryResponse)
                    .toList();
        }

        // Nếu chỉ có keyword
        if (status == null) {
            return categoryRepository.findByCategoryNameContainingIgnoreCase(keyword).stream()
                    .map(categoryMapper::toCategoryResponse)
                    .toList();
        }

        // Nếu có cả status và keyword
        return categoryRepository.findByDelFlagAndCategoryNameContainingIgnoreCase(status, keyword).stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        try {
            // Upload file và lấy URL của hình ảnh
            String imageUrl = fileUploadService.uploadFile(request.getImage());

            Category category = Category.builder()
                    .categoryName(request.getCategoryName())
                    .image(imageUrl)
                    .slug(CreateSlug.createSlug(request.getCategoryName()))
                    .build();

            category = categoryRepository.save(category);

            return categoryMapper.toCategoryResponse(category);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }


    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse updateCategory(String id, CategoryUpdateRequest request) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        categoryMapper.updateCategory(category, request);
        categoryRepository.save(category);
        return categoryMapper.toCategoryResponse(category);
    }

    public void deleteCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.setDelFlag(true);
        categoryRepository.save(category);
    }

    public void restoreCategory(String id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        category.setDelFlag(false);
        categoryRepository.save(category);
    }
}
