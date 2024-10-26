package com.example.auction_web.service.specification;

import com.example.auction_web.entity.Type;
import org.springframework.data.jpa.domain.Specification;

public class TypeSpecification {
    public static Specification<Type> hasTypeNameContaining(String typeName) {
        return (root, query, criteriaBuilder) -> {
            if (typeName == null || typeName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("typeName"), "%" + typeName + "%");
        };
    }

    public static Specification<Type> hasStatus(Boolean status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("delFlag"), status);
        };
    }
}
