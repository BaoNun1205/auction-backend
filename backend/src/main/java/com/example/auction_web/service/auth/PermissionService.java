package com.example.auction_web.service.auth;

import com.example.auction_web.dto.request.auth.PermissionRequest;
import com.example.auction_web.dto.response.auth.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    void delete(String permission);
}
