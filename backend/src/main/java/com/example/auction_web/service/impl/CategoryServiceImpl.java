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
import com.example.auction_web.service.specification.CategorySpecification;
import com.example.auction_web.utils.CreateSlug;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<CategoryResponse> getCategories() {
        return categoryRepository.findCategoriesByDelFlag(false).stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public List<CategoryResponse> filterCategories(Boolean status, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        if (isAllParamsNullOrEmpty(status, keyword)) {
            return categoryRepository.findAll().stream()
                    .map(categoryMapper::toCategoryResponse)
                    .toList();
        }

        Specification<Category> specification = Specification
                .where(CategorySpecification.hasCategoryNameContaining(keyword))
                .and(CategorySpecification.hasStatus(status));

        return categoryRepository.findAll(specification, pageable).stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    public int totalCategories(Boolean status, String keyword) {
        if (isAllParamsNullOrEmpty(status, keyword)) {
            return categoryRepository.findAll().size();
        }

        Specification<Category> specification = Specification
                .where(CategorySpecification.hasCategoryNameContaining(keyword))
                .and(CategorySpecification.hasStatus(status));

        return categoryRepository.findAll(specification).size();
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

    private Boolean isAllParamsNullOrEmpty(Boolean status, String keyword) {
        return status == null && (keyword == null || keyword.isEmpty());
    }
}
