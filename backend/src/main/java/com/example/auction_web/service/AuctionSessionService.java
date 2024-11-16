package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionInfoDetail;
import com.example.auction_web.dto.response.AuctionSessionResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface AuctionSessionService {
    AuctionSessionResponse createAuctionSession(AuctionSessionCreateRequest request);
    AuctionSessionResponse updateAuctionSession(String id, AuctionSessionUpdateRequest request);
    AuctionSessionResponse getAuctionSessionById(String auctionSessionId);
    void completeAuctionSession(String auctionSessionId);
    List<AuctionSessionResponse> getListAuctionSessionByStatus(String status);
    List<AuctionSessionResponse> filterAuctionSession(String status, LocalDateTime fromDate, LocalDateTime toDate, String keyword, Integer page, Integer size);
    int totalAuctionSession(String status, LocalDateTime fromDate, LocalDateTime toDate, String keyword);
    AuctionSessionInfoDetail getDetailAuctionSessionById(String auctionSessionId);
}
