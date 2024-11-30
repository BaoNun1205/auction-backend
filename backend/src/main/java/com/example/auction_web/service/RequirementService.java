package com.example.auction_web.service;

import com.example.auction_web.dto.request.RequirementCreateRequest;
import com.example.auction_web.dto.request.RequirementUpdateRequest;
import com.example.auction_web.dto.response.RequirementResponse;
import com.example.auction_web.entity.Inspector;
import com.example.auction_web.entity.Requirement;
import com.example.auction_web.entity.auth.User;

import java.util.List;

public interface RequirementService {
    RequirementResponse createRequirement(RequirementCreateRequest request);
    RequirementResponse updateRequirement(String requirementId, RequirementUpdateRequest request);
    void approvedRequirement(String requirementId, User inspector);
    void rejectRequirement(String requirementId);
    void deleteRequirement(String requirementId);
    List<RequirementResponse> getRequirementsByVendorId(String vendorId);
    List<RequirementResponse> getRequirementsByInspectorId(String inspectorId);
    RequirementResponse getRequirementResponseById(String requirementId);
    Requirement getRequirementById(String requirementId);
    List<RequirementResponse> filterRequirements(String status, String keyword, Integer page, Integer size);
    int totalRequirements(String status, String keyword);
}
