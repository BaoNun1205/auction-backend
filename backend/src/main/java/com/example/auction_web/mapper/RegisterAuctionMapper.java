package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.RegisterAuctionCreateRequest;
import com.example.auction_web.dto.request.RegisterAuctionUpdateRequest;
import com.example.auction_web.dto.response.RegisterAuctionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.RegisterAuction;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.springframework.web.bind.annotation.RequestMapping;

@Mapper(componentModel = "spring")
public interface RegisterAuctionMapper {
    @Mapping(target = "auctionSession", ignore = true)
    @Mapping(target = "user", ignore = true)
    RegisterAuction toRegisterAuction(RegisterAuctionCreateRequest request);

    @Mapping(target = "auctionSessionId", source = "auctionSession", qualifiedByName = "auctionSessionToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    RegisterAuctionResponse toRegisterAuctionResponse(RegisterAuction registerAuction);

    void updateRegisterAuction(@MappingTarget RegisterAuction registerAuction, RegisterAuctionUpdateRequest request);

    @Named("auctionSessionToString")
    default String auctionSessionToString(AuctionSession auctionSession) {
        return auctionSession != null ? auctionSession.getAuctionSessionId() : null;
    }

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
