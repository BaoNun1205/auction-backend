package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.entity.auth.Insprector;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.RequirementMapper;
import com.example.auction_web.repository.RequirementRepository;
import com.example.auction_web.repository.auth.InsprectorRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.RequirementService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RequirementServiceImpl implements RequirementService {
    RequirementRepository requirementRepository;
    UserRepository userRepository;
    InsprectorRepository insprectorRepository;
    RequirementMapper requirementMapper;

    public RequirementResponse createRequirement(RequirementCreateRequest request) {
        var requirement = requirementMapper.toRequirement(request);
        setRequirementReference(request, requirement);
        return requirementMapper.toRequirementResponse(requirementRepository.save(requirement));
    }

    public RequirementResponse updateRequirement(String requirementId, RequirementUpdateRequest request) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
        requirementMapper.updateRequirement(requirement, request);
        return requirementMapper.toRequirementResponse(requirementRepository.save(requirement));
    }

    public List<RequirementResponse> getAllRequirements() {
        return requirementRepository.findAll().stream()
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public List<RequirementResponse> getRequirementsByVendorId(String vendorId) {
        return requirementRepository.findRequirementsByUser_UserId(vendorId).stream()
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public List<RequirementResponse> getRequirementsByInspectorId(String inspectorId) {
        return requirementRepository.findRequirementsByInsprector_InsprectorId(inspectorId).stream()
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public Requirement getRequirementById(String requirementId) {
        return requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
    }

    public List<RequirementResponse> filterRequirements(Boolean status, String keyword) {
        return requirementRepository.findAll().stream()
                .filter(requirement -> Optional.ofNullable(status)
                        .map(s -> requirement.getStatus().equals(s))
                        .orElse(true))
                .filter(requirement -> Optional.ofNullable(keyword)
                        .map(k -> requirement.getAssetName().toLowerCase().contains(k.toLowerCase()))
                        .orElse(true))
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public RequirementResponse getRequirementResponseById(String requirementId) {
        return requirementMapper.toRequirementResponse(requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED)));
    }

    void setRequirementReference(RequirementCreateRequest requirementCreateRequest, Requirement requirement) {
        requirement.setUser(getVendorById(requirementCreateRequest.getVendorId()));
//        requirement.setInsprector(getInspectorById(requirementCreateRequest.getInspectorId()));
    }

    User getVendorById(String vendorId) {
        return userRepository.findById(vendorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    Insprector getInspectorById(String inspectorId) {
        return insprectorRepository.findById(inspectorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSPECTOR_NOT_EXISTED));
    }
}
