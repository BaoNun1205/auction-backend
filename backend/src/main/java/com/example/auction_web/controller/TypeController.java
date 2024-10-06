package com.example.auction_web.controller;

import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.request.filter.CategoryFilterRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.TypeResponse;
import com.example.auction_web.service.TypeService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/type")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class TypeController {
    TypeService typeService;

    @PostMapping
    ApiResponse<TypeResponse> createType(@ModelAttribute TypeCreateRequest request) {
        return ApiResponse.<TypeResponse>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.createType(request))
                .build();
    }

    @GetMapping
    ApiResponse<List<TypeResponse>> getAllTypes() {
        return ApiResponse.<List<TypeResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.getAllTypes())
                .build();
    }

    @PostMapping("/filter")
    public ApiResponse<List<TypeResponse>> filterCategories(@RequestBody CategoryFilterRequest filterRequest) {
        List<TypeResponse> filteredTypes = typeService.filterTypes(filterRequest.getStatus(), filterRequest.getKeyword());
        return ApiResponse.<List<TypeResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(filteredTypes)
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<TypeResponse> updateType(@PathVariable String id,@RequestBody TypeUpdateRequest request) {
        return ApiResponse.<TypeResponse>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.updateType(id, request))
                .build();
    }

    @GetMapping("/category/{categoryName}")
    ApiResponse<List<TypeResponse>> findTypeByCategory(@RequestParam String categoryName) {
        return ApiResponse.<List<TypeResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.findTypesByCategoryName(categoryName))
                .build();
    }

    @PutMapping("/restore/{typeId}")
    ApiResponse<String> restoreCategory(@PathVariable String typeId) {
        typeService.restoreType(typeId);
        return ApiResponse.<String>builder().result("Type has been restored").build();
    }

    @DeleteMapping("/{typeId}")
    ApiResponse<String> delete(@PathVariable String typeId){
        typeService.deleteType(typeId);
        return ApiResponse.<String>builder().result("Type has been deleted").build();
    }
}
