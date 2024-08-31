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

@Table(name = "coin_user")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CoinUser {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String coinUserId;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    @Column(precision = 15, scale = 0)
    BigDecimal coinQuantity;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
