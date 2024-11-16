package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Event;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuctionSessionMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "asset", ignore = true)
    AuctionSession toAuctionItem(AuctionSessionCreateRequest request);
    List<AuctionSession> toAuctionItems(List<AuctionSessionCreateRequest> requests);
    AuctionSessionResponse toAuctionItemResponse(AuctionSession auctionSession);
    List<AuctionSessionResponse> toAuctionItemResponses(List<AuctionSession> auctionSessions);

    void updateAuctionItem(@MappingTarget AuctionSession auctionSession, AuctionSessionUpdateRequest request);


    @Named("assetToString")
    default String assetToString(Asset asset) {
        return asset != null ? asset.getAssetId() : null;
    }

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
