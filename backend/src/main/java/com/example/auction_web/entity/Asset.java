package com.example.auction_web.entity;

import com.example.auction_web.entity.auth.Insprector;
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

    @ManyToOne
    @JoinColumn(name = "vendorId", referencedColumnName = "userId")
    User user;

    String assetName;
    String slug;
    String mainImage;
    String assetDescription;

    @Column(precision = 15, scale = 0)
    BigDecimal assetPrice;

    @OneToOne
    @JoinColumn(name = "insprectorId", referencedColumnName = "insprectorId")
    Insprector insprector;

    @ManyToOne
    @JoinColumn(name = "typeId", referencedColumnName = "typeId")
    Type type;

    @OneToOne
    @JoinColumn(name = "assetStatusId", referencedColumnName = "assetStatusId")
    AssetStatus assetStatus;

    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    AuctionSession auctionSession;

    @OneToMany(mappedBy = "asset", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<ImageAsset> imageAssets;
}
