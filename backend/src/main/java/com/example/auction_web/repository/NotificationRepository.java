package com.example.auction_web.repository;

import com.example.auction_web.entity.ScheduleLog.NotificationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationLog, String> {
    List<NotificationLog> findByStatus(NotificationLog.NotificationStatus status);
    NotificationLog findNotificationLogByAuctionSessionIdAndEmail(String auctionSessionId, String email);
}
