package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.SESSION_WIN_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "bill")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String billId;

    String transactionCode;

    LocalDateTime billDate;

    @ManyToOne
    @JoinColumn(name = "addressId", referencedColumnName = "addressId")
    Address address;

    @OneToOne
    @JoinColumn(name = "sessionId", referencedColumnName = "auctionSessionId")
    AuctionSession session;

    @ManyToOne
    @JoinColumn(name = "buyerId", referencedColumnName = "userId")
    User buyerBill;

    @ManyToOne
    @JoinColumn(name = "sellerId", referencedColumnName = "userId")
    User sellerBill;

    @Column(precision = 15, scale = 0)
    BigDecimal totalPrice;

    @Column(precision = 15, scale = 0)
    BigDecimal bidPrice;

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
}
