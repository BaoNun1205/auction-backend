package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuctionItemCreateRequest;
import com.example.auction_web.dto.request.AuctionItemUpdateRequest;
import com.example.auction_web.dto.response.AuctionItemResponse;

import java.util.List;

public interface AuctionItemService {
    AuctionItemResponse createAuctionItem(AuctionItemCreateRequest request);
    AuctionItemResponse updateAuctionItem(String id, AuctionItemUpdateRequest request);
    List<AuctionItemResponse> getAllAuctionItems();
    List<AuctionItemResponse> getAuctionItemsByAuctionSessionId(String auctionSessionId);
}
