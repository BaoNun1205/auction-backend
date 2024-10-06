package com.example.auction_web.entity.auth;

import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Requirement;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

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

    @OneToMany(mappedBy = "insprector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Asset> assets;

    @OneToMany(mappedBy = "insprector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Requirement> requirement;
}
