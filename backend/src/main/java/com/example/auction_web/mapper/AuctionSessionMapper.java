package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuctionSessionMapper {
    AuctionSession toAuctionSession(AuctionSessionCreateRequest request);
    List<AuctionSession> toAuctionSessions(List<AuctionSessionCreateRequest> requests);
    AuctionSessionResponse toAuctionSessionResponse(AuctionSession auctionSession);
    List<AuctionSessionResponse> toAuctionSessionResponses(List<AuctionSession> auctionSessions);
    void updateAuctionSession(@MappingTarget AuctionSession auctionSession, AuctionSessionUpdateRequest request);
}
