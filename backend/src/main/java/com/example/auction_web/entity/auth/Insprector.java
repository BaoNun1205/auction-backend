package com.example.auction_web.entity.auth;

import com.example.auction_web.entity.Asset;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Table(name = "inspector")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Insprector {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String insprectorId;

    @OneToOne
    @JoinColumn(name = "userId", referencedColumnName = "userId")
    User user;

    String license;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "insprector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Asset asset;
}
