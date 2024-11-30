package com.example.auction_web.controller;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.CategoryResponse;
import com.example.auction_web.dto.response.DataResponse;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.service.ImageRequirementService;
import com.example.auction_web.service.InspectorService;
import com.example.auction_web.service.RequirementService;
import com.example.auction_web.service.auth.UserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/requirement")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RequirementController {
    RequirementService requirementService;
    ImageRequirementService imageRequirementService;
    UserService userService;

    @PostMapping
    public ApiResponse<RequirementResponse> createRequirement(
            @ModelAttribute RequirementCreateRequest request) {

        // Tạo Requirement
        RequirementResponse requirementResponse = requirementService.createRequirement(request);

        Requirement requirement = requirementService.getRequirementById(requirementResponse.getRequirementId());

        // Tạo ảnh cho Requirement
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            imageRequirementService.createImageRequirement(request.getImages(), requirement);
        }

        return ApiResponse.<RequirementResponse>builder()
                .code(HttpStatus.OK.value())
                .result(requirementResponse)
                .build();
    }

    @GetMapping
    public ApiResponse<DataResponse> filterRequirements(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size) {
        List<RequirementResponse> filteredRequirements = requirementService.filterRequirements(status, keyword, page, size);
        int total = requirementService.totalRequirements(status, keyword);
        return ApiResponse.<DataResponse>builder()
                .code(HttpStatus.OK.value())
                .result(DataResponse.<List<RequirementResponse>>builder()
                        .data(filteredRequirements)
                        .total(total)
                        .build())
                .build();
    }

    @PutMapping("/{requirementId}")
    ApiResponse<RequirementResponse> updateRequirement(@PathVariable String requirementId, @RequestBody RequirementUpdateRequest request) {
        return ApiResponse.<RequirementResponse>builder()
                .code(HttpStatus.OK.value())
                .result(requirementService.updateRequirement(requirementId, request))
                .build();
    }

    @DeleteMapping("/{requirementId}")
    ApiResponse<String> deleteRequirement(@PathVariable String requirementId) {
        requirementService.deleteRequirement(requirementId);
        return ApiResponse.<String>builder().result("Requirement has been deleted").build();
    }

    @PutMapping("/approved")
    ApiResponse<String> approvedRequirement(
            @RequestParam String requirementId,
            @RequestParam String inspectorId) {

        requirementService.approvedRequirement(requirementId, userService.getUser(inspectorId));
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Approved requirement successfully")
                .build();
    }

    @PutMapping("/rejected/{requirementId}")
    ApiResponse<String> rejectRequirement(@PathVariable String requirementId) {
        requirementService.rejectRequirement(requirementId);
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result("Reject requirement successfully")
                .build();
    }

    @GetMapping("/vendor/{vendorId}")
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
                .result(requirementService.getRequirementResponseById(requirementId))
                .build();
    }
}
