package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.dto.response.SessionWinnerResponse;
import com.example.auction_web.dto.response.auth.UserResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.SessionWinner;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface SessionWinnerMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "auctionSession", ignore = true)
    SessionWinner toSessionWinner(SessionWinnerCreateRequest request);

    @Mapping(target = "user", source = "user", qualifiedByName = "userToResponse")
    @Mapping(target = "auctionSession", source = "auctionSession", qualifiedByName = "auctionSessionToResponse")
    SessionWinnerResponse toSessionWinnerResponse(SessionWinner sessionWinner);

    @Named("userToResponse")
    default UserResponse userToResponse(User user) {
        if (user == null) {
            return null;
        }
        return UserResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phone(user.getPhone())
                .name(user.getName())
                .build();
    }

    @Named("auctionSessionToResponse")
    default AuctionSessionResponse auctionSessionToResponse(AuctionSession auctionSession) {
        if (auctionSession == null) {
            return null;
        }
        return AuctionSessionResponse.builder()
                .auctionSessionId(auctionSession.getAuctionSessionId())
                .name(auctionSession.getName())
                .description(auctionSession.getDescription())
                .startTime(auctionSession.getStartTime())
                .endTime(auctionSession.getEndTime())
                .startingBids(auctionSession.getStartingBids())
                .bidIncrement(auctionSession.getBidIncrement())
                .depositAmount(auctionSession.getDepositAmount())
                .status(auctionSession.getStatus())
                .build();
    }
}
