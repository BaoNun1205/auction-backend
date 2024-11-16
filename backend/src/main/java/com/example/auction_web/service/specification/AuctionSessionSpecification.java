package com.example.auction_web.service.specification;

import com.example.auction_web.entity.AuctionSession;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class AuctionSessionSpecification {
    public static Specification<AuctionSession> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
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
                return criteriaBuilder.conjunction(); // Không áp dụng điều kiện nào nếu từ khóa trống
            }

            // Thực hiện join với asset để truy cập vào trường assetName
            Join<Object, Object> asset = root.join("asset");
            System.out.println("asset: " + asset.get("assetName").toString());

            // Tạo mẫu tìm kiếm với ký tự escape cho các ký tự đặc biệt
            String searchPattern = "%" + keyword + "%";

            // Sử dụng criteriaBuilder.like để thực hiện tìm kiếm với ký tự escape
            return criteriaBuilder.like(
                    criteriaBuilder.lower(asset.get("assetName")),
                    searchPattern.toLowerCase()
            );
        };
    }

}
