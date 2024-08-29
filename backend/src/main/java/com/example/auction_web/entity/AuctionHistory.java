package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "auction_item")
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

    @OneToOne
    @JoinColumn(name = "auctionItemId", referencedColumnName = "auctionItemId")
    AuctionItem auctionItem;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "auctionHistory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AuctionHistoryDetail> auctionHistoryDetails;
}
