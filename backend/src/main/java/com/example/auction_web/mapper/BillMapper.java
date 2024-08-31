package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillUpdateRequest;
import com.example.auction_web.dto.response.BillResponse;
import com.example.auction_web.entity.Bill;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillMapper {
    Bill toBill(BillCreateRequest request);
    List<Bill> toBills(List<BillCreateRequest> requests);
    BillResponse toBillResponse(Bill bill);
    List<BillResponse> toBillResponses(List<Bill> bills);
    void updateBill(@MappingTarget Bill bill, BillUpdateRequest request);
}
