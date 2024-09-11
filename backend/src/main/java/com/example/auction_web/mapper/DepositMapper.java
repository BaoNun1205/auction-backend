package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    @Mapping(target = "auctionItem", ignore = true)
    @Mapping(target = "user", ignore = true)
    Deposit toDeposit(DepositCreateRequest request);
    List<Deposit> toDeposits(List<DepositCreateRequest> requests);

    @Mapping(target = "auctionItemId", source = "auctionItem", qualifiedByName = "auctionItemToString")
    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    DepositResponse toDepositResponse(Deposit deposit);
    List<DepositResponse> toDepositResponses(List<Deposit> deposits);
    void updateDeposit(@MappingTarget Deposit deposit, DepositUpdateRequest request);

    @Named("auctionItemToString")
    default String auctionItemToString(AuctionItem auctionItem) {
        return auctionItem != null ? auctionItem.getAuctionItemId() : null;
    }

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }
}
