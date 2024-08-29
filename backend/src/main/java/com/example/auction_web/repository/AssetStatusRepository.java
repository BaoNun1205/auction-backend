package com.example.auction_web.repository;

import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AssetStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetStatusRepository extends JpaRepository<AssetStatus, String> {
}
