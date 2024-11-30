package com.example.auction_web.service;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;

import java.util.List;

public interface TypeService {
    List<TypeResponse> filterTypes(Boolean status, String keyword, Integer page, Integer size);
    int totalTypes(Boolean status, String keyword);
    TypeResponse getTypeById(String id);
    TypeResponse createType(TypeCreateRequest request);
    TypeResponse updateType(String typeId, TypeUpdateRequest request);
    List<TypeResponse> getTypes();
    void deleteType(String id);
    void restoreType(String id);
}
