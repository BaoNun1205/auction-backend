package com.example.auction_web.service.specification;

import com.example.auction_web.entity.Requirement;
import org.springframework.data.jpa.domain.Specification;

public class RequirementSpecification {
    public static Specification<Requirement> hasDelFlagFalse() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("delFlag"));
    }
    public static Specification<Requirement> hasAssetNameContaining(String assetName) {
        return (root, query, criteriaBuilder) -> {
            if (assetName == null || assetName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("assetName"), "%" + assetName + "%");
        };
    }

    public static Specification<Requirement> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
