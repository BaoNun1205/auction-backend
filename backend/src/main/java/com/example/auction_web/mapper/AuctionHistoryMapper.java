package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AuctionHistoryMapper {
    @Mapping(target = "auctionItem", ignore = true)
    AuctionHistory toAuctionHistory(AuctionHistoryCreateRequest request);

    @Mapping(target = "auctionItemId", source = "auctionItem", qualifiedByName = "auctionItemToString")
    AuctionHistoryResponse toAuctionHistoryResponse(AuctionHistory auctionHistory);
    void updateAuctionHistoryFromRequest(@MappingTarget AuctionHistory auctionHistory, AuctionHistoryUpdateRequest request);

    @Named("auctionItemToString")
    default String auctionItemToString(AuctionItem auctionItem) {
        return auctionItem != null ? auctionItem.getAuctionItemId() : null;
    }
}
