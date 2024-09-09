package com.example.auction_web.repository;

import com.example.auction_web.entity.ImageRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRequirementRepository extends JpaRepository<ImageRequirement, String> {
    List<ImageRequirement> findImageRequirementsByRequirement_RequirementId(String requirementId);
}
