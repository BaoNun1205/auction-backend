package com.example.auction_web.service.specification;

import com.example.auction_web.entity.AuctionSession;
import org.springframework.data.jpa.domain.Specification;

public class RelatedSessionSpecification {
    public static Specification<AuctionSession> hasType(String type) {
        return (root, query, criteriaBuilder) -> {
            if (type == null || type.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("asset").get("type").get("typeId"), type);
        };
    }

    public static Specification<AuctionSession> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null || status.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<AuctionSession> hasAuctionSessionNotEqual(String auctionSessionId) {
        return (root, query, criteriaBuilder) -> {
            if (auctionSessionId == null || auctionSessionId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.notEqual(root.get("auctionSessionId"), auctionSessionId);
        };
    }
}
