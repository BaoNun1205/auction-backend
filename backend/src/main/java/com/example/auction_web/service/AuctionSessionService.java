package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;

import java.util.List;

public interface AuctionSessionService {
    AuctionSessionResponse createAuctionSession(AuctionSessionCreateRequest request);
    AuctionSessionResponse updateAuctionSession(String id, AuctionSessionUpdateRequest request);
    List<AuctionSessionResponse> getAllAuctionSessions();
}
