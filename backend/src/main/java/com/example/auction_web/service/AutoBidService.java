package com.example.auction_web.service;

import com.example.auction_web.dto.request.AutoBidCreateRequest;
import com.example.auction_web.dto.request.AutoBidUpdateRequest;
import com.example.auction_web.dto.response.AutoBidResponse;

public interface AutoBidService {
    AutoBidResponse createAutoBid(AutoBidCreateRequest request);
    Boolean checkAutoBid(String auctionSessionId, String userId);
    AutoBidResponse getAutoBidByAuctionSessionIdAndUserId(String auctionSessionId, String userId);
    AutoBidResponse updateAutoBid(String id, AutoBidUpdateRequest request);
}
