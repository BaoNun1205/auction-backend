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
import com.example.auction_web.service.FileUploadService;
import com.example.auction_web.service.ImageRequirementService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ImageRequirementServiceImpl implements ImageRequirementService {
    ImageRequirementRepository imageRequirementRepository;
    RequirementRepository requirementRepository;
    ImageRequirementMapper imageRequirementMapper;
    FileUploadService fileUploadService;

    public void createImageRequirement(List<MultipartFile> images, Requirement requirement) {
        List<ImageRequirement> imageRequirements = new ArrayList<>();

        try {
            List<String> imageUrls = images.stream()
                    .map(image -> {
                        try {
                            return fileUploadService.uploadFile(image);
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to upload image: " + image.getOriginalFilename(), e);
                        }
                    })
                    .collect(Collectors.toList());

            for (String imageUrl : imageUrls) {
                ImageRequirement imageRequirement = new ImageRequirement();
                imageRequirement.setImage(imageUrl);
                imageRequirement.setRequirement(requirement);

                imageRequirements.add(imageRequirement);
            }

            imageRequirementRepository.saveAll(imageRequirements);

        } catch (RuntimeException e) {
            throw new AppException(ErrorCode.FILE_UPLOAD_FAILED);
        }
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
