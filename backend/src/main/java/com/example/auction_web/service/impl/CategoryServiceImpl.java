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
                .filter(category -> !category.getDelFlag())
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse createCategory(CategoryCreateRequest request) {
        try {
            // Tải ảnh lên Cloudinary và lấy URL
            String imageUrl = fileUploadService.uploadFile(request.getImage()); // Giả sử bạn có trường MultipartFile image trong request

            // Chuyển đổi request thành category
//            Category category = categoryMapper.toCategory(request);
            Category category = new Category();
            category.setCategoryName(request.getCategoryName());
            category.setImage(imageUrl);

            // Cập nhật slug cho category
            String slug = CreateSlug.createSlug(category.getCategoryName());
            category.setSlug(slug);

            // Lưu category vào database
            category = categoryRepository.save(category);

            return categoryMapper.toCategoryResponse(category);
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi nếu cần
            return null; // Hoặc ném ra một ngoại lệ thích hợp
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
}
