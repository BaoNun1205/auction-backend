package com.example.auction_web.repository;

import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import com.example.auction_web.entity.ScheduleLog.PaymentCancelLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCancelRepository extends JpaRepository<PaymentCancelLog, String> {
    List<PaymentCancelLog> findByStatus(NotificationLog.NotificationStatus status);
    PaymentCancelLog findPaymentCancelLogByAuctionSessionIdAndSellerId(String auctionSessionId, String sellerId);
}
