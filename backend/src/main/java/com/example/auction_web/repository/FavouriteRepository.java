package com.example.auction_web.repository;

import com.example.auction_web.entity.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, String> {
    List<Favourite> findFavouritesByUser_UserId(String userId);
    List<Favourite> findFavouritesByAsset_AssetId(String assetId);
}
