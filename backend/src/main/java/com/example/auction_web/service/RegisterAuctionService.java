package com.example.auction_web.service;

import com.example.auction_web.dto.request.RegisterAuctionCreateRequest;
import com.example.auction_web.dto.request.RegisterAuctionUpdateRequest;
import com.example.auction_web.dto.response.RegisterAuctionResponse;

import java.util.List;

public interface RegisterAuctionService {
    RegisterAuctionResponse createRegisterAuction(RegisterAuctionCreateRequest request);
    RegisterAuctionResponse updateRegisterAuction(String registerAuctionId, RegisterAuctionUpdateRequest request);
    List<RegisterAuctionResponse> findAllRegisterAuctions();
    List<RegisterAuctionResponse> findRegisterAuctionByUserId(String userId);
    RegisterAuctionResponse findRegisterAuctionById(String id);
    RegisterAuctionResponse findRegisterAuctionByAuctionSessionId(String auctionSessionId);
}
