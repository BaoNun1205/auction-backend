package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.entity.Type;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TypeMapper {
    Type toType(TypeCreateRequest request);
    List<Type> toTypes(List<TypeCreateRequest> requests);
    TypeResponse toTypeResponse(Type type);
    List<TypeResponse> toTypeResponses(List<Type> types);
    void updateType(@MappingTarget Type type, TypeUpdateRequest request);
}
