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

@Table(name = "deposit")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Deposit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String depositId;

    @ManyToOne
    @JoinColumn(name = "auctionSessionId", referencedColumnName = "auctionSessionId")
    AuctionSession auctionSession;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    @Column(precision = 15, scale = 0)
    BigDecimal depositPrice;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "deposit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Bill bill;
}
