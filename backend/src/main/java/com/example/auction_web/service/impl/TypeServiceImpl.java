package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.entity.Category;
import com.example.auction_web.entity.Type;
import com.example.auction_web.mapper.TypeMapper;
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
    TypeMapper typeMapper;

    public TypeResponse createType(TypeCreateRequest typeCreateRequest) {
        return typeMapper.toTypeResponse(typeRepository.save(typeMapper.toType(typeCreateRequest)));
    }

    public TypeResponse updateType(String id, TypeUpdateRequest request) {
        Type type = typeRepository.findById(id).orElseThrow();
        typeMapper.updateType(type, request);
        return typeMapper.toTypeResponse(typeRepository.save(type));
    }

    public List<TypeResponse> findAllTypes() {
        return typeRepository.findAll().stream()
                .map(typeMapper::toTypeResponse)
                .toList();
    }

    public List<TypeResponse> findTypesByCategory(Category category) {
        return typeRepository.findTypesByCategory(category).stream()
                .map(typeMapper::toTypeResponse)
                .toList();
    }
}
