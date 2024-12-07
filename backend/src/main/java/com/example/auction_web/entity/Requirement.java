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
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String requirementId;

    @ManyToOne
    @JoinColumn(name = "vendorId", referencedColumnName = "userId")
    User vendor;

    String assetName;

    @Lob
    @Column(columnDefinition = "TEXT")
    String assetDescription;

    BigDecimal assetPrice;

    @ManyToOne
    @JoinColumn(name = "inspectorId", referencedColumnName = "userId")
    User inspector;

    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
        this.status = "0";
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "requirement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ImageRequirement> imageRequirements;

    @OneToOne(mappedBy = "requirement", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Asset asset;
}
