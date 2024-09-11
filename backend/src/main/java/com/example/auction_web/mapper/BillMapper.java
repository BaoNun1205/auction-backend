package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Address;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.auth.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillMapper {
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "address", ignore = true)
    Bill toBill(BillCreateRequest request);
    List<Bill> toBills(List<BillCreateRequest> requests);

    @Mapping(target = "userId", source = "user", qualifiedByName = "userToString")
    @Mapping(target = "addressId", source = "address", qualifiedByName = "addressToString")
    BillResponse toBillResponse(Bill bill);
    List<BillResponse> toBillResponses(List<Bill> bills);
    void updateBill(@MappingTarget Bill bill, BillUpdateRequest request);

    @Named("userToString")
    default String userToString(User user) {
        return user != null ? user.getUserId() : null;
    }

    @Named("addressToString")
    default String addressToString(Address address) {
        return address != null ? address.getAddressId() : null;
    }
}
