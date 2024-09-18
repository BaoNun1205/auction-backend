package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.RegisterSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.RegisterSession;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface RegisterSessionMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "auctionSession", ignore = true)
    RegisterSession toRegisterSession(RegisterSessionCreateRequest registerSessionCreateRequest);

    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    @Mapping(target = "auctionSessionId", source = "auctionSession", qualifiedByName = "auctionSessionToString")
    RegisterSessionResponse toRegisterSessionResponse(RegisterSession registerSession);

    void updateRegisterSession(@MappingTarget RegisterSession registerSession, RegisterSessionCreateRequest registerSessionCreateRequest);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("auctionSessionToString")
    default String auctionSessionToString(AuctionSession auctionSession) {
        return auctionSession != null ? auctionSession.getAuctionSessionId() : null;
    }
}
