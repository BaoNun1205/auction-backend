package com.example.auction_web.service.specification;

import com.example.auction_web.entity.AuctionSession;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AuctionSessionSpecification {
    public static Specification<AuctionSession> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<AuctionSession> hasFromDateToDate(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("startTime"), fromDate, toDate);
    }

    public static Specification<AuctionSession> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("asset").get("assetName"), "%" + keyword + "%");
        };
    }
}
