package com.example.auction_web.service;

import com.example.auction_web.dto.request.ImageRequirementCreateRequest;
import com.example.auction_web.dto.request.ImageRequirementUpdateRequest;
import com.example.auction_web.dto.response.ImageRequirementResponse;
import com.example.auction_web.entity.Requirement;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageRequirementService {
    void createImageRequirement(List<MultipartFile> images, Requirement requirement);
    ImageRequirementResponse updateImageRequirement(String imageRequirementId, ImageRequirementUpdateRequest request);
    List<ImageRequirementResponse> getAllImageRequirements();
    List<ImageRequirementResponse> getImageRequirementsByRequirementId(String requirementId);
}
