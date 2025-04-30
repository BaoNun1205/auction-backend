package com.example.auction_web.entity.auth;

import com.example.auction_web.entity.*;
import com.example.auction_web.entity.chat.Conversation;
import com.example.auction_web.entity.chat.Message;
import com.example.auction_web.enums.GENDER;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Table(name = "user")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String userId;

    @Column(unique = true, columnDefinition = "VARCHAR(255) COLLATE utf8mb4_unicode_ci")
    String username;

    String name;
    String password;
    String avatar;
    String email;
    String phone;

    @Enumerated(EnumType.STRING)
    GENDER gender;

    Long unreadNotificationCount;

    @Column(name = "last_seen")
    LocalDateTime lastSeen;

    Long totalResponseCount;       // Tổng số lần đã phản hồi
    Long responseTimeInSeconds;    // Trung bình phản hồi
    LocalDateTime lastRespontimeCalculatedAt;

    Long totalOpponentMessages;         // Tổng số tin nhắn đối phương gửi đến user
    Long totalOpponentMessagesReplied;  // Tổng số lần user đã phản hồi lại
    Double responseRate;                // Tỉ lệ phản hồi (theo %)
    LocalDateTime lastResponRateCalculatedAt;

    LocalDate dateOfBirth;
    String token;
    Boolean enabled;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.unreadNotificationCount = 0L;
        this.totalResponseCount = 0L;
        this.totalOpponentMessages = 0L;
        this.totalOpponentMessagesReplied = 0L;
        this.enabled = false;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    Set<Role> roles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<AuctionSession> auctionSessions;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Asset> assets;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    BalanceUser balanceUser;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Inspector inspector;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Deposit> deposits;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AuctionHistory> auctionHistories;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Address> addresses;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Requirement> vendorRequirements;

    @OneToMany(mappedBy = "inspector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Requirement> inspectorRequirements;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<RegisterSession> registerSessions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<SessionWinner> sessionWinners;

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Conversation> buyerConversations;

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Conversation> sellerConversations;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<AutoBid> autoBids;
}