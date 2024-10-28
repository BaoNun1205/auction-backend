package com.example.auction_web.service;

import com.example.auction_web.dto.request.InspectorCreateRequest;
import com.example.auction_web.dto.request.InspectorUpdateRequest;
import com.example.auction_web.dto.response.InspectorResponse;
import com.example.auction_web.entity.Inspector;

import java.util.List;

public interface InspectorService {
    InspectorResponse createInspector(InspectorCreateRequest request);
    InspectorResponse updateInspector(String id, InspectorUpdateRequest request);
    Inspector getInspectorById(String id);
    List<InspectorResponse> getAllInspectors();
    InspectorResponse getInspectorByUserId(String userId);
}
