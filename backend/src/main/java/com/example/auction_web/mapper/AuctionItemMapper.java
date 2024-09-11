package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionItemCreateRequest;
import com.example.auction_web.dto.request.AuctionItemUpdateRequest;
import com.example.auction_web.dto.response.AuctionItemResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.AuctionSession;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuctionItemMapper {
    @Mapping(target = "auctionSession", ignore = true)
    @Mapping(target = "asset", ignore = true)
    AuctionItem toAuctionItem(AuctionItemCreateRequest request);
    List<AuctionItem> toAuctionItems(List<AuctionItemCreateRequest> requests);

    @Mapping(target = "auctionSessionId", source = "auctionSession", qualifiedByName = "auctionSessionToString")
    @Mapping(target = "assetId", source = "asset", qualifiedByName = "assetToString")
    AuctionItemResponse toAuctionItemResponse(AuctionItem auctionItem);
    List<AuctionItemResponse> toAuctionItemResponses(List<AuctionItem> auctionItems);

    void updateAuctionItem(@MappingTarget AuctionItem auctionItem, AuctionItemUpdateRequest request);

    @Named("auctionSessionToString")
    default String auctionSessionToString(AuctionSession auctionSession) {
        return auctionSession != null ? auctionSession.getAuctionSessionId() : null;
    }

    @Named("assetToString")
    default String assetToString(Asset asset) {
        return asset != null ? asset.getAssetId() : null;
    }
}
