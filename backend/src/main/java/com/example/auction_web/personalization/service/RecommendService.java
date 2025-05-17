package com.example.auction_web.personalization.service;

import java.util.List;

import com.example.auction_web.dto.response.AuctionSessionResponse;

public interface RecommendService {
    void batchUpdateUserVectors();
    void batchUpdateAuctionSessionVectors();
    List<AuctionSessionResponse> recommendAuctionSessionResponses(String userId, String status);
    List<AuctionSessionResponse> recommendAuctionSessionResponsesByAuctionSessionId(String auctionSessionId);
}
