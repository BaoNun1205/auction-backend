package com.example.auction_web.service;

import com.example.auction_web.dto.request.InsprectorCreateRequest;
import com.example.auction_web.dto.request.InsprectorUpdateRequest;
import com.example.auction_web.dto.response.InsprectorResponse;
import com.example.auction_web.entity.auth.User;

import java.util.List;

public interface InsprectorService {
    InsprectorResponse createInsprector(InsprectorCreateRequest request);
    InsprectorResponse updateInsprector(String id, InsprectorUpdateRequest request);
    List<InsprectorResponse> getAllInsprectors();
    InsprectorResponse getInsprectorByUserId(String userId);
}
