package com.example.auction_web.mapper;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.entity.Deposit;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DepositMapper {
    Deposit toDeposit(DepositCreateRequest request);
    List<Deposit> toDeposits(List<DepositCreateRequest> requests);
    DepositResponse toDepositResponse(Deposit deposit);
    List<DepositResponse> toDepositResponses(List<Deposit> deposits);
    void updateDeposit(@MappingTarget Deposit deposit, DepositUpdateRequest request);
}
