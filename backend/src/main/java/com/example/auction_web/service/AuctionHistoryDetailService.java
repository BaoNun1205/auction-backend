package com.example.auction_web.service;

import com.example.auction_web.dto.request.AuctionHistoryDetailCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryDetailDeleteRequest;
import com.example.auction_web.dto.response.AuctionHistoryDetailResponse;
import lombok.experimental.FieldDefaults;

import java.util.List;

@FieldDefaults(level = lombok.AccessLevel.PUBLIC)
public interface AuctionHistoryDetailService {
    AuctionHistoryDetailResponse createAuctionHistoryDetail(AuctionHistoryDetailCreateRequest request);
    AuctionHistoryDetailResponse deleteAuctionHistoryDetail(String id, AuctionHistoryDetailDeleteRequest request);
    List<AuctionHistoryDetailResponse> getAllByAuctionHistoryId(String auctionHistoryId);
    List<AuctionHistoryDetailResponse> getAllAuctionHistoryDetails();
}
