package com.example.auction_web.service;

import com.example.auction_web.dto.request.ImageRequirementCreateRequest;
import com.example.auction_web.dto.request.ImageRequirementUpdateRequest;
import com.example.auction_web.dto.response.ImageRequirementResponse;

import java.util.List;

public interface ImageRequirementService {
    ImageRequirementResponse createImageRequirement(ImageRequirementCreateRequest request);
    ImageRequirementResponse updateImageRequirement(String imageRequirementId, ImageRequirementUpdateRequest request);
    List<ImageRequirementResponse> getAllImageRequirements();
    List<ImageRequirementResponse> getImageRequirementsByRequirementId(String requirementId);
}
