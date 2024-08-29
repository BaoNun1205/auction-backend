package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "auction_session")
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
    String typeSession;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToMany(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AuctionItem> auctionItems;

    @OneToOne(mappedBy = "auctionSession", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    RegisterAuction registerAuction;
}
