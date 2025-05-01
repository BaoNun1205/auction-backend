package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.entity.Category;
import com.example.auction_web.entity.Type;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

import com.example.auction_web.dto.response.TypeFilterResponse;

@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface TypeMapper {
    // Phương thức ánh xạ từ String (categoryId) sang Category
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "image", ignore = true)
    Type toType(TypeCreateRequest request);

    List<Type> toTypes(List<TypeCreateRequest> requests);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")
    TypeResponse toTypeResponse(Type type);

    TypeFilterResponse toTypeFilterResponse(Type type);

    List<TypeResponse> toTypeResponses(List<Type> types);
    void updateType(@MappingTarget Type type, TypeUpdateRequest request);

    @Named("categoryToString")
    default String categoryToString(Category category) {
        if (category == null) {
            return null;
        }
        return category.getCategoryId();
    }
}
