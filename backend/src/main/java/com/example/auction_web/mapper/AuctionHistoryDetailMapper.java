package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionHistoryDetailCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryDetailDeleteRequest;
import com.example.auction_web.dto.response.AuctionHistoryDetailResponse;
import com.example.auction_web.entity.AuctionHistoryDetail;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuctionHistoryDetailMapper {
    AuctionHistoryDetail toAuctionHistoryDetail(AuctionHistoryDetailCreateRequest request);
    List<AuctionHistoryDetail> toAuctionHistoryDetails(List<AuctionHistoryDetailCreateRequest> requests);
    AuctionHistoryDetailResponse toAuctionHistoryDetailResponse(AuctionHistoryDetail auctionHistoryDetail);
    List<AuctionHistoryDetailResponse> toAuctionHistoryDetailResponses(List<AuctionHistoryDetail> auctionHistoryDetails);
    void deleteAuctionHistoryDetail(@MappingTarget AuctionHistoryDetail auctionHistoryDetail, AuctionHistoryDetailDeleteRequest request);
}
