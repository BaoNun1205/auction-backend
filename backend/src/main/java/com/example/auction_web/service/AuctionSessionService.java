package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionInfoDetail;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.entity.auth.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AuctionSessionService {
    AuctionSessionResponse createAuctionSession(AuctionSessionCreateRequest request);
    AuctionSessionResponse updateAuctionSession(String id, AuctionSessionUpdateRequest request);
    AuctionSessionResponse getAuctionSessionById(String auctionSessionId);
    List<AuctionSessionResponse> getListAuctionSessionByStatus(String status);
    List<AuctionSessionResponse> filterAuctionSession(String status, String typeId, String userId, LocalDateTime fromDate, LocalDateTime toDate, BigDecimal minPrice, BigDecimal maxPrice, String keyword, Boolean isInCrease, Integer page, Integer size);
    List<AuctionSessionResponse> filterAuctionSessionRelated(String auctionSessionId);
    int totalAuctionSession(String status, LocalDateTime fromDate, LocalDateTime toDate, String keyword, Boolean isInCrease);
    AuctionSessionInfoDetail getDetailAuctionSessionById(String auctionSessionId);
    AuctionSessionInfoDetail getDetailAuctionSessionByAssetId(String assetId);
    List<User> getUsersBiddingInSession(String sessionId);
}
