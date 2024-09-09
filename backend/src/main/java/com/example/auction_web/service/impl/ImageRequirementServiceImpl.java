package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.ImageRequirementCreateRequest;
import com.example.auction_web.dto.request.ImageRequirementUpdateRequest;
import com.example.auction_web.dto.response.ImageRequirementResponse;
import com.example.auction_web.entity.ImageRequirement;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.ImageRequirementMapper;
import com.example.auction_web.repository.ImageRequirementRepository;
import com.example.auction_web.repository.RequirementRepository;
import com.example.auction_web.service.ImageRequirementService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ImageRequirementServiceImpl implements ImageRequirementService {
    ImageRequirementRepository imageRequirementRepository;
    RequirementRepository requirementRepository;
    ImageRequirementMapper imageRequirementMapper;


    public ImageRequirementResponse createImageRequirement(ImageRequirementCreateRequest request) {
        var imageRequirement = imageRequirementMapper.toImageRequirement(request);
        setImageRequirementReference(request, imageRequirement);
        return imageRequirementMapper.toImageRequirementResponse(imageRequirementRepository.save(imageRequirement));
    }

    public ImageRequirementResponse updateImageRequirement(String imageRequirementId, ImageRequirementUpdateRequest request) {
        var imageRequirement = imageRequirementRepository.findById(imageRequirementId)
                .orElseThrow(() -> new AppException(ErrorCode.IMAGE_REQUIREMENT_NOT_EXISTED));
        imageRequirementMapper.updateImageRequirement(imageRequirement, request);
        return imageRequirementMapper.toImageRequirementResponse(imageRequirementRepository.save(imageRequirement));
    }

    public List<ImageRequirementResponse> getAllImageRequirements() {
        return imageRequirementRepository.findAll().stream()
                .map(imageRequirementMapper::toImageRequirementResponse)
                .toList();
    }

    public List<ImageRequirementResponse> getImageRequirementsByRequirementId(String requirementId) {
        return imageRequirementRepository.findImageRequirementsByRequirement_RequirementId(requirementId).stream()
                .map(imageRequirementMapper::toImageRequirementResponse)
                .toList();
    }

    void setImageRequirementReference(ImageRequirementCreateRequest request, ImageRequirement imageRequirement) {
        imageRequirement.setRequirement(getRequirementById(request.getRequirementId()));
    }

    Requirement getRequirementById(String requirementId) {
        return requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
    }
}
