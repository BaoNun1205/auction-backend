package com.example.auction_web.controller;

import com.example.auction_web.dto.request.ImageRequirementCreateRequest;
import com.example.auction_web.dto.request.ImageRequirementUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.ImageRequirementResponse;
import com.example.auction_web.service.ImageRequirementService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/image-requirement")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class ImageRequirementController {
    ImageRequirementService imageRequirementService;

    @PutMapping("/{imageRequirementId}")
    ApiResponse<ImageRequirementResponse> updateImageRequirement(@PathVariable String imageRequirementId, @RequestBody ImageRequirementUpdateRequest request) {
        return ApiResponse.<ImageRequirementResponse>builder()
                .code(HttpStatus.OK.value())
                .result(imageRequirementService.updateImageRequirement(imageRequirementId, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<ImageRequirementResponse>> getAllImageRequirements() {
        return ApiResponse.<List<ImageRequirementResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(imageRequirementService.getAllImageRequirements())
                .build();
    }

    @GetMapping("/requirement/id/{requirementId}")
    ApiResponse<List<ImageRequirementResponse>> getImageRequirementsByRequirementId(@PathVariable String requirementId) {
        return ApiResponse.<List<ImageRequirementResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(imageRequirementService.getImageRequirementsByRequirementId(requirementId))
                .build();
    }
}
