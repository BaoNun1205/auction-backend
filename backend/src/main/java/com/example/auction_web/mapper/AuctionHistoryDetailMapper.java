package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionHistoryDetailCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryDetailDeleteRequest;
import com.example.auction_web.dto.response.AuctionHistoryDetailResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionHistoryDetail;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuctionHistoryDetailMapper {
    @Mapping(target = "auctionHistory", ignore = true)
    @Mapping(target = "user", ignore = true)
    AuctionHistoryDetail toAuctionHistoryDetail(AuctionHistoryDetailCreateRequest request);
    List<AuctionHistoryDetail> toAuctionHistoryDetails(List<AuctionHistoryDetailCreateRequest> requests);

    @Mapping(target = "auctionHistoryId", source = "auctionHistory", qualifiedByName = "auctionHistoryToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    AuctionHistoryDetailResponse toAuctionHistoryDetailResponse(AuctionHistoryDetail auctionHistoryDetail);
    List<AuctionHistoryDetailResponse> toAuctionHistoryDetailResponses(List<AuctionHistoryDetail> auctionHistoryDetails);
    void deleteAuctionHistoryDetail(@MappingTarget AuctionHistoryDetail auctionHistoryDetail, AuctionHistoryDetailDeleteRequest request);

    @Named("auctionHistoryToString")
    default String auctionHistoryToString(AuctionHistory auctionHistory) {
        return auctionHistory != null ? auctionHistory.getAuctionHistoryId() : null;
    }

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
