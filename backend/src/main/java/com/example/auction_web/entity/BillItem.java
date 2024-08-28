package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "bill")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class BillItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String billItemId;

    @ManyToOne
    @JoinColumn(name = "billId", referencedColumnName = "billId")
    Bill bill;

    @OneToOne
    @JoinColumn(name = "assetId", referencedColumnName = "assetId")
    Asset asset;

    @Column(precision = 15, scale = 0)
    BigDecimal profitPrice;

    @Column(precision = 15, scale = 0)
    BigDecimal bidPrice;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
