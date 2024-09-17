package com.example.auction_web.service;

import com.example.auction_web.dto.request.FavouriteCreateRequest;
import com.example.auction_web.dto.request.FavouriteUpdateRequest;
import com.example.auction_web.dto.response.FavouriteResponse;

import java.util.List;

public interface FavouriteService {
    FavouriteResponse createFavourite(FavouriteCreateRequest request);
    FavouriteResponse updateFavourite(String favouriteId, FavouriteUpdateRequest request);
    List<FavouriteResponse> getAllFavourites();
    List<FavouriteResponse> getFavouritesByUserId(String userId);
    List<FavouriteResponse> getFavouritesByAssetId(String assetId);
}
