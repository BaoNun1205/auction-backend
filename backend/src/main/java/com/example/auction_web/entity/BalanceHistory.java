package com.example.auction_web.entity;

import com.example.auction_web.enums.ACTIONBALANCE;
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
public class BalanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String balanceHistoryId;

    @ManyToOne
    @JoinColumn(name = "balanceUserId")
    BalanceUser balanceUser;

    @Column(precision = 15, scale = 0)
    BigDecimal amount;

    String description;
    @Enumerated(EnumType.STRING)
    ACTIONBALANCE actionbalance;
    
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
}
