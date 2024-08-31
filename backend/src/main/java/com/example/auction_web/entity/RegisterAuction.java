package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "register_auction")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class RegisterAuction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String registerAuctionId;

    @ManyToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    @OneToOne
    @JoinColumn(name = "auctionSessionId", referencedColumnName = "auctionSessionId")
    AuctionSession auctionSession;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
