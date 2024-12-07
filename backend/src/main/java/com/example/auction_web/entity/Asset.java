package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ASSET_STATUS;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Table(name = "asset")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String assetId;

    @OneToOne
    @JoinColumn(name = "requirementId", referencedColumnName = "requirementId")
    Requirement requirement;

    @ManyToOne
    @JoinColumn(name = "vendorId", referencedColumnName = "userId")
    User vendor;

    String assetName;
    String slug;
    String mainImage;

    @Lob
    @Column(columnDefinition = "TEXT")
    String assetDescription;

    @Column(precision = 15, scale = 0)
    BigDecimal assetPrice;

    @ManyToOne
    @JoinColumn(name = "inspectorId", referencedColumnName = "inspectorId")
    Inspector inspector;

    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "typeId")
    Type type;

    String status;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.delFlag = false;
        this.status = ASSET_STATUS.NOT_AUCTIONED.toString();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    AuctionSession auctionSession;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    List<ImageAsset> imageAssets;
}
