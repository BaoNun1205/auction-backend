package com.example.auction_web.controller;

import com.cloudinary.Api;
import com.example.auction_web.dto.request.TypeCreateRequest;
import com.example.auction_web.dto.request.TypeUpdateRequest;
import com.example.auction_web.dto.request.filter.CategoryFilterRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.DataResponse;
import com.example.auction_web.dto.response.TypeFilterResponse;
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

    @GetMapping("/get-all")
    ApiResponse<List<TypeResponse>> getTypes(){
        return ApiResponse.<List<TypeResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.getTypes())
                .build();
    }

    @GetMapping
    public ApiResponse<DataResponse> filterCategories(
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false, defaultValue = "0") Integer page,
            @RequestParam(required = false, defaultValue = "10") Integer size) {
        List<TypeResponse> filteredTypes = typeService.filterTypes(status, keyword, page, size);
        int total = typeService.totalTypes(status, keyword);
        return ApiResponse.<DataResponse>builder()
                .code(HttpStatus.OK.value())
                .result(DataResponse.<List<TypeResponse>>builder()
                        .data(filteredTypes)
                        .build())
                .build();
    }

    @GetMapping("/{id}")
    ApiResponse<TypeResponse> getTypeById(@PathVariable String id) {
        return ApiResponse.<TypeResponse>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.getTypeById(id))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<TypeResponse> updateType(@PathVariable String id,@RequestBody TypeUpdateRequest request) {
        return ApiResponse.<TypeResponse>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.updateType(id, request))
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

    @GetMapping("/get-all-type-filter-response")
    ApiResponse<List<TypeFilterResponse>> getAllTypeFilterResponse() {
        return ApiResponse.<List<TypeFilterResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(typeService.getAllTypeFilterResponse())
                .build();
    }
}
