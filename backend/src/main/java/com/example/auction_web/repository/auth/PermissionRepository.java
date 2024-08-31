package com.example.auction_web.repository.auth;

import com.example.auction_web.entity.auth.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, String> {
}
