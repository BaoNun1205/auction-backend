package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.enums.AUTOBID;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AutoBid {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String autoBidId;

    @ManyToOne
    @JoinColumn(name = "userId")
    User user;

    @ManyToOne
    @JoinColumn(name = "auctionSessionId")
    AuctionSession auctionSession;

    @Column(precision = 15, scale = 0)
    BigDecimal maxBidPrice;

    @Column(precision = 15, scale = 0)
    BigDecimal bidIncrement;

    @Enumerated(EnumType.STRING)
    AUTOBID status;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
        this.status = AUTOBID.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
