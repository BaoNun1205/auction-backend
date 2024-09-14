package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    @Mapping(target = "auctionSession", ignore = true)
    @Mapping(target = "user", ignore = true)
    Deposit toDeposit(DepositCreateRequest request);
    List<Deposit> toDeposits(List<DepositCreateRequest> requests);

    @Mapping(target = "auctionSessionId", source = "auctionSession", qualifiedByName = "auctionSessionToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    DepositResponse toDepositResponse(Deposit deposit);
    List<DepositResponse> toDepositResponses(List<Deposit> deposits);
    void updateDeposit(@MappingTarget Deposit deposit, DepositUpdateRequest request);

    @Named("auctionSessionToString")
    default String auctionSessionToString(AuctionSession auctionSession) {
        return auctionSession != null ? auctionSession.getAuctionSessionId() : null;
    }

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
