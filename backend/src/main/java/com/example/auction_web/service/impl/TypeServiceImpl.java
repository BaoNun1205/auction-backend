package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.entity.Category;
import com.example.auction_web.entity.Type;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.TypeMapper;
import com.example.auction_web.repository.CategoryRepository;
import com.example.auction_web.repository.TypeRepository;
import com.example.auction_web.service.FileUploadService;
import com.example.auction_web.service.TypeService;
import com.example.auction_web.utils.CreateSlug;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TypeServiceImpl implements TypeService {
    TypeRepository typeRepository;
    CategoryRepository categoryRepository;
    TypeMapper typeMapper;
    FileUploadService fileUploadService;

    public List<TypeResponse> filterTypes(Boolean status, String keyword) {
        if (status == null && (keyword == null || keyword.isEmpty())) {
            return typeRepository.findAll().stream()
                    .map(typeMapper::toTypeResponse)
                    .toList();
        }

        // Nếu chỉ có status
        if (status != null && (keyword == null || keyword.isEmpty())) {
            return typeRepository.findByDelFlag(status).stream()
                    .map(typeMapper::toTypeResponse)
                    .toList();
        }

        // Nếu chỉ có keyword
        if (status == null) {
            return typeRepository.findByTypeNameContainingIgnoreCase(keyword).stream()
                    .map(typeMapper::toTypeResponse)
                    .toList();
        }

        // Nếu có cả status và keyword
        return typeRepository.findByDelFlagAndTypeNameContainingIgnoreCase(status, keyword).stream()
                .map(typeMapper::toTypeResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public TypeResponse createType(TypeCreateRequest request) {
        try {
            String imageUrl = fileUploadService.uploadFile(request.getImage());

            Type type = Type.builder()
                    .typeName(request.getTypeName())
                    .category(categoryRepository.findById(request.getCategory()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED)))
                    .image(imageUrl)
                    .slug(CreateSlug.createSlug(request.getTypeName()))
                    .build();

            type = typeRepository.save(type);

            return typeMapper.toTypeResponse(type);
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
    }

    public TypeResponse updateType(String id, TypeUpdateRequest request) {
        Type type = typeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED));
        typeMapper.updateType(type, request);
        return typeMapper.toTypeResponse(typeRepository.save(type));
    }

    public List<TypeResponse> getAllTypes() {
        return typeRepository.findAll().stream()
                .map(typeMapper::toTypeResponse)
                .toList();
    }

    public void deleteType(String id) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED));
        type.setDelFlag(true);
        typeRepository.save(type);
    }

    public void restoreType(String id) {
        Type type = typeRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED));
        type.setDelFlag(false);
        typeRepository.save(type);
    }

    public List<TypeResponse> findTypesByCategoryName(String categoryName) {
        if (!categoryRepository.existsByCategoryName(categoryName)) {
            throw new AppException(ErrorCode.CATEGORY_NOT_EXISTED);
        }
        return typeRepository.findTypesByCategory_CategoryName(categoryName).stream()
                .map(typeMapper::toTypeResponse)
                .toList();
    }
}
