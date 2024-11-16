package com.example.auction_web.entity.ScheduleLog;

import com.example.auction_web.enums.AUCTION_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String auctionSessionId;

    private LocalDateTime scheduledTime;

    private LocalDateTime sentTime;

    private String currentStatus;

    @Enumerated(EnumType.STRING)
    private SessionLog.SessionLogStatus status;

    public enum SessionLogStatus {
        SCHEDULED, SENT, FAILED
    }
}
