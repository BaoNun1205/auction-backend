package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionItemCreateRequest;
import com.example.auction_web.dto.request.AuctionItemUpdateRequest;
import com.example.auction_web.dto.response.AuctionItemResponse;
import com.example.auction_web.entity.AuctionItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuctionItemMapper {
    AuctionItem toAuctionItem(AuctionItemCreateRequest request);
    List<AuctionItem> toAuctionItems(List<AuctionItemCreateRequest> requests);
    AuctionItemResponse toAuctionItemResponse(AuctionItem auctionItem);
    List<AuctionItemResponse> toAuctionItemResponses(List<AuctionItem> auctionItems);
    void updateAuctionItem(@MappingTarget AuctionItem auctionItem, AuctionItemUpdateRequest request);
}
