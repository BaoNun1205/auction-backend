package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.SessionHistoryResponse;

import java.util.List;

public interface AuctionHistoryService {
    AuctionHistoryResponse createAuctionHistory(AuctionHistoryCreateRequest request);
    AuctionHistoryResponse updateAuctionHistory(String id, AuctionHistoryUpdateRequest request);
    List<AuctionHistoryResponse> getAllAuctionHistories();
    AuctionHistoryResponse getAuctionHistoriesByAuctionSessionId(String auctionSessionId);
    AuctionSessionInfoResponse getAuctionSessionInfo(String auctionSessionId);
    List<SessionHistoryResponse> getSessionsHistoryByAuctionSessionId(String auctionSessionId);
}
