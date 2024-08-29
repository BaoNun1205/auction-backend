package com.example.auction_web.repository;

import com.example.auction_web.entity.District;
import com.example.auction_web.entity.ImageAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageAssetRepository extends JpaRepository<ImageAsset, String> {
}
