package com.example.auction_web.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "asset_status")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AssetStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String assetStatusId;
    String assetStatusName;
    Boolean delFlag;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    @OneToOne(mappedBy = "assetStatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Asset asset;

    @OneToMany(mappedBy = "assetStatus", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Requirement> requirement;
}
