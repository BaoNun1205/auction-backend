package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
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

    @Mapping(target = "user", source = "user", qualifiedByName = "userToResponse")
    @Mapping(target = "auctionSession", source = "auctionSession", qualifiedByName = "auctionSessionToResponse")
    RegisterSessionResponse toRegisterSessionResponse(RegisterSession registerSession);

    void updateRegisterSession(@MappingTarget RegisterSession registerSession, RegisterSessionCreateRequest registerSessionCreateRequest);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("auctionSessionToResponse")
    default AuctionSessionResponse auctionSessionToResponse(AuctionSession auctionSession) {
        if (auctionSession == null) return null;
        return AuctionSessionResponse.builder()
                .auctionSessionId(auctionSession.getAuctionSessionId())
                .name(auctionSession.getName())
                .description(auctionSession.getDescription())
                .startTime(auctionSession.getStartTime())
                .endTime(auctionSession.getEndTime())
                .status(auctionSession.getStatus())
                .startingBids(auctionSession.getStartingBids())
                .build();
    }

    @Named("mapAuctionSession")
    default AuctionSession map(AuctionSession auctionSession) {
        if (auctionSession == null) return null;
        return AuctionSession.builder()
                .auctionSessionId(auctionSession.getAuctionSessionId())
                .name(auctionSession.getName())
                .description(auctionSession.getDescription())
                .startTime(auctionSession.getStartTime())
                .endTime(auctionSession.getEndTime())
                .status(auctionSession.getStatus())
                .startingBids(auctionSession.getStartingBids())
                .build();
    }

    @Named("userToResponse")
    default User userToResponse(User user) {
        if (user == null) return null;
        return User.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .name(user.getName())
                .phone(user.getPhone())
                .email(user.getEmail())
                .build();
    }
}
