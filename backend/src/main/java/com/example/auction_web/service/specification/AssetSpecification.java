package com.example.auction_web.service.specification;

import com.example.auction_web.entity.Asset;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class AssetSpecification {

    public static Specification<Asset> hasVendorId(String vendorId) {
        return (root, query, criteriaBuilder) -> {
            if (vendorId == null || vendorId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("userId"), vendorId);
        };
    }

    public static Specification<Asset> hasAssetNameContaining(String assetName) {
        return (root, query, criteriaBuilder) -> {
            if (assetName == null || assetName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("assetName"), "%" + assetName + "%");
        };
    }

    public static Specification<Asset> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, query, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            } else if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(root.get("assetPrice"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("assetPrice"), minPrice);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("assetPrice"), maxPrice);
            }
        };
    }

    public static Specification<Asset> hasInsprectorId(String insprectorId) {
        return (root, query, criteriaBuilder) -> {
            if (insprectorId == null || insprectorId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("insprector").get("insprectorId"), insprectorId);
        };
    }

    public static Specification<Asset> hasTypeId(String typeId) {
        return (root, query, criteriaBuilder) -> {
            if (typeId == null || typeId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("type").get("typeId"), typeId);
        };
    }

    public static Specification<Asset> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
}
