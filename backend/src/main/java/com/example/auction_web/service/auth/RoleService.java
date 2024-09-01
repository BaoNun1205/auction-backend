package com.example.auction_web.service.auth;

import com.example.auction_web.dto.request.auth.RoleRequest;
import com.example.auction_web.dto.response.auth.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(String role);
}
