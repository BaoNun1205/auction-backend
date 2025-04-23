package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "acution_history")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String auctionHistoryId;

    @ManyToOne
    @JoinColumn(name = "auctionSessionId", referencedColumnName = "auctionSessionId")
    AuctionSession auctionSession;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    BigDecimal bidPrice;
    LocalDateTime bidTime;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @Version
    Long version;

    @PrePersist
    protected void onCreate() {
        this.bidTime = LocalDateTime.now();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
        if (this.version == null) {
            this.version = 0L;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
