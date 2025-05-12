package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.AUCTION_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionSession {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String auctionSessionId;

    String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    String description;

    @ManyToMany
    List<Event> events;
    String typeSession;

    @OneToOne
    @JoinColumn(name = "assetId", referencedColumnName = "assetId")
    Asset asset;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    LocalDateTime startTime;
    LocalDateTime endTime;

    @Column(precision = 15, scale = 0)
    BigDecimal startingBids;

    @Column(precision = 15, scale = 0)
    BigDecimal bidIncrement;

    @Column(precision = 15, scale = 0)
    BigDecimal depositAmount;

    @Column(columnDefinition = "TEXT")
    String vectorJson;

    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
        this.status = AUCTION_STATUS.UPCOMING.toString();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Deposit> deposits;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AuctionHistory> auctionHistories;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<RegisterSession> registerSessions;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<SessionWinner> sessionWinners;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AutoBid> autoBids;
}
