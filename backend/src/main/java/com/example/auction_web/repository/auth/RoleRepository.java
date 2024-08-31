package com.example.auction_web.repository.auth;

import com.example.auction_web.entity.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
}
