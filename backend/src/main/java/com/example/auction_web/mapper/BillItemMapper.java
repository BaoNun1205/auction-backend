package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.BillItemCreateRequest;
import com.example.auction_web.dto.request.BillItemUpdateRequest;
import com.example.auction_web.dto.response.BillItemResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.BillItem;
import com.example.auction_web.entity.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BillItemMapper {
    @Mapping(target = "bill", ignore = true)
    @Mapping(target = "asset", ignore = true)
    @Mapping(target = "deposit", ignore = true)
    BillItem toBillItem(BillItemCreateRequest request);
    List<BillItem> toBillItems(List<BillItemCreateRequest> requests);

    @Mapping(target = "billId", source = "bill", qualifiedByName = "billToString")
    @Mapping(target = "assetId", source = "asset", qualifiedByName = "assetToString")
    @Mapping(target = "depositId", source = "deposit", qualifiedByName = "depositToString")
    BillItemResponse toBillItemResponse(BillItem billItem);
    List<BillItemResponse> toBillItemResponses(List<BillItem> billItems);
    void updateBillItem(@MappingTarget BillItem billItem, BillItemUpdateRequest request);

    @Named("billToString")
    default String billToString(Bill bill) {
        return bill != null ? bill.getBillId() : null;
    }

    @Named("assetToString")
    default String assetToString(Asset asset) {
        return asset != null ? asset.getAssetId() : null;
    }

    @Named("depositToString")
    default String depositToString(Deposit deposit) {
        return deposit != null ? deposit.getDepositId() : null;
    }
}
