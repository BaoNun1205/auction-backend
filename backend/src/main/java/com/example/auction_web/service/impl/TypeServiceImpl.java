package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.entity.Type;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.TypeMapper;
import com.example.auction_web.repository.CategoryRepository;
import com.example.auction_web.repository.TypeRepository;
import com.example.auction_web.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TypeServiceImpl implements TypeService {
    TypeRepository typeRepository;
    CategoryRepository categoryRepository;
    TypeMapper typeMapper;

    public TypeResponse createType(TypeCreateRequest request) {
        var type = typeMapper.toType(request);

        if (request.getCategory() != null) {
            var category = categoryRepository.findById(request.getCategory()).orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
            type.setCategory(category);
        }
        return typeMapper.toTypeResponse(typeRepository.save(type));
    }

    public TypeResponse updateType(String id, TypeUpdateRequest request) {
        Type type = typeRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.TYPE_NOT_EXISTED));
        typeMapper.updateType(type, request);
        return typeMapper.toTypeResponse(typeRepository.save(type));
    }

    public List<TypeResponse> findAllTypes() {
        return typeRepository.findAll().stream()
                .map(typeMapper::toTypeResponse)
                .toList();
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
