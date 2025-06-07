package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillMapper {
    @Mapping(target = "buyerBill", ignore = true)
    @Mapping(target = "sellerBill", ignore = true)
    @Mapping(target = "session", ignore = true)
    @Mapping(target = "address", ignore = true)
    Bill toBill(BillCreateRequest request);
    List<Bill> toBills(List<BillCreateRequest> requests);

    // @Mapping(target = "buyerId", source = "buyerBill", qualifiedByName = "userToString")
    // @Mapping(target = "sellerId", source = "sellerBill", qualifiedByName = "userToString")
    // @Mapping(target = "sessionId", source = "session", qualifiedByName = "sessionToString")
    // @Mapping(target = "addressId", source = "address", qualifiedByName = "addressToString")
    BillResponse toBillResponse(Bill bill);
    List<BillResponse> toBillResponses(List<Bill> bills);

    @Mapping(target = "buyerBill", ignore = true)
    @Mapping(target = "sellerBill", ignore = true)
    @Mapping(target = "session", ignore = true)
    @Mapping(target = "address", ignore = true)
    void updateBill(@MappingTarget Bill bill, BillUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("sessionToString")
    default String sessionToString(AuctionSession auctionSession) {
        return auctionSession != null ? auctionSession.getAuctionSessionId() : null;
    }

    @Named("addressToString")
    default String addressToString(Address address) {
        return address != null ? address.getAddressId() : null;
    }
}
