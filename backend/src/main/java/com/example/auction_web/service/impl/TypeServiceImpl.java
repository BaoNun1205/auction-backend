package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeFilterResponse;
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
import com.example.auction_web.service.specification.TypeSpecification;
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

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TypeServiceImpl implements TypeService {
    TypeRepository typeRepository;
    CategoryRepository categoryRepository;
    TypeMapper typeMapper;
    FileUploadService fileUploadService;

    public List<TypeResponse> filterTypes(Boolean status, String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (isAllParamsNullOrEmpty(status, keyword)) {
            return typeRepository.findAll().stream()
                    .map(typeMapper::toTypeResponse)
                    .toList();
        }

        Specification<Type> specification = Specification
                .where(TypeSpecification.hasTypeNameContaining(keyword))
                .and(TypeSpecification.hasStatus(status));

        return typeRepository.findAll(specification, pageable).stream()
                .map(typeMapper::toTypeResponse)
                .toList();
    }

    public int totalTypes(Boolean status, String keyword) {
        if (isAllParamsNullOrEmpty(status, keyword)) {
            return typeRepository.findAll().size();
        }

        Specification<Type> specification = Specification
                .where(TypeSpecification.hasTypeNameContaining(keyword))
                .and(TypeSpecification.hasStatus(status));

        return typeRepository.findAll(specification).size();
    }

    public TypeResponse getTypeById(String id) {
        return typeMapper.toTypeResponse(typeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED)));
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

    public List<TypeResponse> getTypes() {
        return typeRepository.findTypesByDelFlag(false).stream()
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

    private Boolean isAllParamsNullOrEmpty(Boolean status, String keyword) {
        return status == null && (keyword == null || keyword.isEmpty());
    }

    @Override
    public List<TypeFilterResponse> getAllTypeFilterResponse() {
        return typeRepository.findTypesByDelFlag(false).stream()
                .map(typeMapper::toTypeFilterResponse)
                .toList();
    }
}
