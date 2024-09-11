package com.example.auction_web.service;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;

import java.util.List;

public interface DepositService {
    DepositResponse createDeposit(DepositCreateRequest request);
    DepositResponse updateDeposit(String Id, DepositUpdateRequest request);
    List<DepositResponse> findAllDeposits();
    List<DepositResponse> findDepositByAuctionItemId(String auctionItemId);
    List<DepositResponse> findDepositByUserId(String userId);
}
