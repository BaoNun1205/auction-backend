package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.FavouriteCreateRequest;
import com.example.auction_web.dto.request.FavouriteUpdateRequest;
import com.example.auction_web.dto.response.FavouriteResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Favourite;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface FavouriteMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "asset", ignore = true)
    Favourite toFavourite(FavouriteCreateRequest favouriteCreateRequest);

    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    @Mapping(target = "assetId", source = "asset", qualifiedByName = "assetToString")
    FavouriteResponse toFavouriteResponse(Favourite favourite);

    void updateFavourite(@MappingTarget Favourite favourite, FavouriteUpdateRequest favouriteUpdateRequest);

    @Named("userToString")
    default String userToString(User user) {
        return user.getUserId();
    }

    @Named("assetToString")
    default String assetToString(Asset asset) {
        return asset.getAssetId();
    }
}
