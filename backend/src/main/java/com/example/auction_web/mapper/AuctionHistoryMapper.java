package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface AuctionHistoryMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "auctionSession", ignore = true)
    AuctionHistory toAuctionHistory(AuctionHistoryCreateRequest request);

    @Mapping(target = "auctionSessionId", source = "auctionSession", qualifiedByName = "auctionSessionToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "userIdToString")
    AuctionHistoryResponse toAuctionHistoryResponse(AuctionHistory auctionHistory);
    void updateAuctionHistoryFromRequest(@MappingTarget AuctionHistory auctionHistory, AuctionHistoryUpdateRequest request);

    @Named("auctionSessionToString")
    default String auctionSessionToString(AuctionSession auctionSession) {
        return auctionSession.getAuctionSessionId();
    }

    @Named("userIdToString")
    default String userIdToString(User user) {
        return user.getUserId();
    }
}
