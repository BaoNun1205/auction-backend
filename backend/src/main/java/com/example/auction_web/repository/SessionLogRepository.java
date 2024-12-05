package com.example.auction_web.repository;

import com.example.auction_web.entity.ScheduleLog.SessionLog;
import com.example.auction_web.enums.AUCTION_STATUS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionLogRepository extends JpaRepository<SessionLog, String> {
    List<SessionLog> findByStatus(SessionLog.SessionLogStatus status);
    SessionLog findSessionLogByAuctionSessionIdAndCurrentStatus(String auctionSessionId, String currentStatus);
    void deleteAllByAuctionSessionId(String auctionSessionId);
}
