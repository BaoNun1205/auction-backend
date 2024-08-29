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
public class AuctionItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String auctionItemId;

    @ManyToOne
    @JoinColumn(name = "auctionSessionId", referencedColumnName = "auctionSessionId")
    AuctionSession auctionSession;

    @OneToOne
    @JoinColumn(name = "assetId", referencedColumnName = "assetId")
    Asset asset;

    @Column(precision = 15, scale = 0)
    BigDecimal startingBids;

    @Column(precision = 15, scale = 0)
    BigDecimal bidIncrement;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "auctionItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Deposit> deposits;

    @OneToOne(mappedBy = "auctionItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    AuctionHistory auctionHistory;
}
