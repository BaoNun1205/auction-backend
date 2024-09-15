package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.FavouriteCreateRequest;
import com.example.auction_web.dto.request.FavouriteUpdateRequest;
import com.example.auction_web.dto.response.FavouriteResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Favourite;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.FavouriteMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.FavouriteRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.FavouriteService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class FavouriteServiceImpl implements FavouriteService {
    FavouriteRepository favouriteRepository;
    UserRepository userRepository;
    AssetRepository assetRepository;

    FavouriteMapper favouriteMapper;


    public FavouriteResponse createFavourite(FavouriteCreateRequest request) {
        Favourite favourite = favouriteMapper.toFavourite(request);
        setFavouriteReferences(request, favourite);
        return favouriteMapper.toFavouriteResponse(favouriteRepository.save(favourite));
    }

    public FavouriteResponse updateFavourite(String favouriteId, FavouriteUpdateRequest request) {
        Favourite favourite = favouriteRepository.findById(favouriteId)
                .orElseThrow(() -> new AppException(ErrorCode.FAVOURITE_NOT_EXISTED));
        favouriteMapper.updateFavourite(favourite, request);
        return favouriteMapper.toFavouriteResponse(favouriteRepository.save(favourite));
    }

    public List<FavouriteResponse> getAllFavourites() {
        return favouriteRepository.findAll().stream()
                .map(favouriteMapper::toFavouriteResponse)
                .toList();
    }

    public List<FavouriteResponse> getFavouritesByUserId(String userId) {
        return favouriteRepository.findFavouritesByUser_UserId(userId).stream()
                .map(favouriteMapper::toFavouriteResponse)
                .toList();
    }

    public List<FavouriteResponse> getFavouritesByAssetId(String assetId) {
        return favouriteRepository.findFavouritesByAsset_AssetId(assetId).stream()
                .map(favouriteMapper::toFavouriteResponse)
                .toList();
    }

    void setFavouriteReferences(FavouriteCreateRequest request, Favourite favourite) {
        favourite.setUser(getUserById(request.getUserId()));
        favourite.setAsset(getAssetById(request.getAssetId()));
    }

    User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    Asset getAssetById(String assetId) {
        return assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
    }
}
