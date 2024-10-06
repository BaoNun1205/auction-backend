package com.example.auction_web.repository;

import com.example.auction_web.entity.Asset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssetRepository extends JpaRepository<Asset, String>, JpaSpecificationExecutor<Asset> {
    List<Asset> findByAssetNameContaining(String assetName);
    Boolean existsByAssetNameContaining(String assetName);
}
