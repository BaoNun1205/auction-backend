package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.dto.response.BalanceHistoryResponse;
import com.example.auction_web.entity.BalanceHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BalanceHistoryMapper {
    BalanceHistory toBalanceHistory(BalanceHistoryCreateRequest request);

    BalanceHistoryResponse toBalanceHistoryResponse(BalanceHistory balanceHistory);
}
