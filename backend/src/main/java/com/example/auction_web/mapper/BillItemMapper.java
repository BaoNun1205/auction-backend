package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillItemCreateRequest;
import com.example.auction_web.dto.request.BillItemUpdateRequest;
import com.example.auction_web.dto.response.BillItemResponse;
import com.example.auction_web.entity.BillItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillItemMapper {
    BillItem toBillItem(BillItemCreateRequest request);
    List<BillItem> toBillItems(List<BillItemCreateRequest> requests);
    BillItemResponse toBillItemResponse(BillItem billItem);
    List<BillItemResponse> toBillItemResponses(List<BillItem> billItems);
    void updateBillItem(@MappingTarget BillItem billItem, BillItemUpdateRequest request);
}
