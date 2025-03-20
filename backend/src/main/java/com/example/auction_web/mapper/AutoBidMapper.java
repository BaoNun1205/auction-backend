package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.AutoBidCreateRequest;
import com.example.auction_web.dto.request.AutoBidUpdateRequest;
import com.example.auction_web.dto.response.AutoBidResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.AutoBid;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.lang.annotation.Target;

@Mapper(componentModel = "spring")
public interface AutoBidMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "auctionSession", ignore = true)
    AutoBid toAutoBid(AutoBidCreateRequest request);

    @Mapping(source = "user", target = "userId", qualifiedByName = "userToString")
    @Mapping(source = "auctionSession", target = "auctionSessionId", qualifiedByName = "auctionSessionToString")
    AutoBidResponse toAutoBidResponse(AutoBid autoBid);

    void updateAutoBid(@MappingTarget AutoBid autoBid, AutoBidUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("auctionSessionToString")
    default String auctionSessionToString(AuctionSession auctionSession) {
        return auctionSession != null ? auctionSession.getAuctionSessionId() : null;
    }
}
