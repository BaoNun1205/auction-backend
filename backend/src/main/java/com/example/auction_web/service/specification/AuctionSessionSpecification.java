package com.example.auction_web.service.specification;

import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AuctionSessionSpecification {
    public static Specification<AuctionSession> hasUserId(String userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null || userId.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("user").get("userId"), userId);
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

    public static Specification<AuctionSession> hasFromDateToDate(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate != null && toDate != null) {
                // Tìm trong khoảng từ 'fromDate' đến 'toDate'
                return criteriaBuilder.between(root.get("startTime"), fromDate, toDate);
            } else if (fromDate != null) {
                // Chỉ có 'fromDate', tìm các phiên bắt đầu từ 'fromDate' trở đi
                return criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), fromDate);
            } else if (toDate != null) {
                // Chỉ có 'toDate', tìm các phiên kết thúc trước 'toDate'
                return criteriaBuilder.lessThanOrEqualTo(root.get("startTime"), toDate);
            }
            return criteriaBuilder.conjunction(); // Không có điều kiện nào được cung cấp, trả về luôn true
        };
    }


    public static Specification<AuctionSession> hasKeyword(String keyword) {
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
        };
    }

    public static Specification<AuctionSession> hasIsInCrease(Boolean isInCrease) {
        return (root, query, criteriaBuilder) -> {
            if (isInCrease != null) {
                if (isInCrease) {
                    query.orderBy(criteriaBuilder.asc(root.get("startingBids")));
                } else {
                    query.orderBy(criteriaBuilder.desc(root.get("startingBids")));
                }
            }
            return null; // Trả về null để không thêm bất kỳ điều kiện lọc nào
        };
    }


}
