package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.RequirementMapper;
import com.example.auction_web.repository.InspectorRepository;
import com.example.auction_web.repository.RequirementRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.RequirementService;
import com.example.auction_web.service.specification.RequirementSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class RequirementServiceImpl implements RequirementService {
    RequirementRepository requirementRepository;
    UserRepository userRepository;
    InspectorRepository inspectorRepository;
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

    public void approvedRequirement(String requirementId, User inspector) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
        requirement.setStatus("1");
        requirement.setInspector(inspector);
        requirementRepository.save(requirement);
    }

    public void rejectRequirement(String requirementId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
        requirement.setStatus("2");
        requirementRepository.save(requirement);
    }

    public void deleteRequirement(String requirementId) {
        Requirement requirement = requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
        requirement.setDelFlag(true);
        requirementRepository.save(requirement);
    }

    public List<RequirementResponse> getRequirementsByVendorId(String vendorId) {
        return requirementRepository.findRequirementsByVendor_UserIdAndDelFlagFalse(vendorId).stream()
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public List<RequirementResponse> getRequirementsByInspectorId(String inspectorId) {
        return requirementRepository.findRequirementsByInspector_UserId(inspectorId).stream()
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public Requirement getRequirementById(String requirementId) {
        return requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED));
    }

    public List<RequirementResponse> filterRequirements(String status, String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);

        if (isAllParamsNullOrEmpty(status, keyword)) {
            // Thêm điều kiện deflag = false khi không có tham số tìm kiếm
            Specification<Requirement> defaultSpec = Specification.where(RequirementSpecification.hasDelFlagFalse());
            return requirementRepository.findAll(defaultSpec, pageable).stream()
                    .map(requirementMapper::toRequirementResponse)
                    .toList();
        }

        // Kết hợp các điều kiện
        Specification<Requirement> specification = Specification
                .where(RequirementSpecification.hasDelFlagFalse()) // Điều kiện deflag = false
                .and(RequirementSpecification.hasStatus(status))
                .and(RequirementSpecification.hasAssetNameContaining(keyword));

        // Thực hiện query
        return requirementRepository.findAll(specification, pageable).stream()
                .map(requirementMapper::toRequirementResponse)
                .toList();
    }

    public int totalRequirements(String status, String keyword) {
        if (isAllParamsNullOrEmpty(status, keyword)) {
            return requirementRepository.findAll().size();
        }

        Specification<Requirement> specification = Specification
                .where(RequirementSpecification.hasStatus(status))
                .and(RequirementSpecification.hasAssetNameContaining(keyword));

        return requirementRepository.findAll(specification).size();
    }

    public RequirementResponse getRequirementResponseById(String requirementId) {
        return requirementMapper.toRequirementResponse(requirementRepository.findById(requirementId)
                .orElseThrow(() -> new AppException(ErrorCode.REQUIREMENT_NOT_EXISTED)));
    }

    void setRequirementReference(RequirementCreateRequest requirementCreateRequest, Requirement requirement) {
        requirement.setVendor(getVendorById(requirementCreateRequest.getVendorId()));
//        requirement.setInsprector(getInspectorById(requirementCreateRequest.getInspectorId()));
    }

    User getVendorById(String vendorId) {
        return userRepository.findById(vendorId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    Inspector getInspectorById(String inspectorId) {
        return inspectorRepository.findById(inspectorId)
                .orElseThrow(() -> new AppException(ErrorCode.INSPECTOR_NOT_EXISTED));
    }

    private Boolean isAllParamsNullOrEmpty(String status, String keyword) {
        return (status == null || status.isEmpty()) && (keyword == null || keyword.isEmpty());
    }
}
