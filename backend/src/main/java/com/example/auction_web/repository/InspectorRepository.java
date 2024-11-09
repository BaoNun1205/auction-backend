package com.example.auction_web.repository;

import com.example.auction_web.entity.Inspector;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InspectorRepository extends JpaRepository<Inspector,String> {
    Inspector findInspectorByUser_UserId(String userId);
}
