package com.example.auction_web.service;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.entity.Category;

import java.util.List;

public interface TypeService {
    TypeResponse createType(TypeCreateRequest request);
    TypeResponse updateType(String typeId, TypeUpdateRequest request);
    List<TypeResponse> findAllTypes();
    List<TypeResponse> findTypesByCategory(Category category);
}
