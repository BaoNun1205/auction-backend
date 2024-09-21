package com.example.auction_web.controller;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.service.RequirementService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/requirement")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RequirementController {
    RequirementService requirementService;

    @PostMapping
    ApiResponse<RequirementResponse> createRequirement(@RequestBody RequirementCreateRequest request) {
        return ApiResponse.<RequirementResponse>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.createRequirement(request))
                .build();
    }

    @PutMapping("/{requirementId}")
    ApiResponse<RequirementResponse> updateRequirement(@PathVariable String requirementId, @RequestBody RequirementUpdateRequest request) {
        return ApiResponse.<RequirementResponse>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.updateRequirement(requirementId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<RequirementResponse>> getAllRequirements() {
        return ApiResponse.<List<RequirementResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.getAllRequirements())
                .build();
    }

    @GetMapping("/vendor/id/{vendorId}")
    ApiResponse<List<RequirementResponse>> getRequirementsByVendorId(@PathVariable String vendorId) {
        return ApiResponse.<List<RequirementResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.getRequirementsByVendorId(vendorId))
                .build();
    }

    @GetMapping("/inspector/id/{inspectorId}")
    ApiResponse<List<RequirementResponse>> getRequirementsByInspectorId(@PathVariable String inspectorId) {
        return ApiResponse.<List<RequirementResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.getRequirementsByInspectorId(inspectorId))
                .build();
    }

    @GetMapping("/{requirementId}")
    ApiResponse<RequirementResponse> getRequirementById(@PathVariable String requirementId) {
        return ApiResponse.<RequirementResponse>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.getRequirementById(requirementId))
                .build();
    }
}
