package com.example.auction_web.service.specification;

import com.example.auction_web.entity.Category;
import org.springframework.data.jpa.domain.Specification;

public class CategorySpecification {
    public static Specification<Category> hasCategoryNameContaining(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("categoryName"), "%" + categoryName + "%");
        };
    }

    public static Specification<Category> hasStatus(Boolean status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("delFlag"), status);
        };
    }
}
