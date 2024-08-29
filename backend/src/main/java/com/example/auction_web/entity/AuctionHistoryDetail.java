package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "acution_history_detail")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AuctionHistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String auctionHistoryDetailId;

    @ManyToOne
    @JoinColumn(name = "auctionHistoryId", referencedColumnName = "auctionHistoryId")
    AuctionHistory auctionHistory;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    @Column(precision = 15, scale = 0)
    BigDecimal auctionPrice;

    LocalDateTime auctionTime;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

}
