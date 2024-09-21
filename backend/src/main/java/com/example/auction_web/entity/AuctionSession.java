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

    @ManyToMany
    List<Event> events;
    String typeSession;

    @OneToOne
    @JoinColumn(name = "assetId", referencedColumnName = "assetId")
    Asset asset;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    LocalDateTime startTime;
    LocalDateTime endTime;

    @Column(precision = 15, scale = 0)
    BigDecimal startingBids;

    @Column(precision = 15, scale = 0)
    BigDecimal bidIncrement;

    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Deposit> deposits;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AuctionHistory> auctionHistories;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<RegisterSession> registerSessions;
}
