package com.example.auction_web.service;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.dto.response.UsersJoinSessionResponse;

import java.math.BigDecimal;
import java.util.List;

public interface DepositService {
    DepositResponse createDeposit(DepositCreateRequest request);
    DepositResponse updateDeposit(String Id, DepositUpdateRequest request);
    Boolean checkDeposit(String auctionSessionId, String userId);
    List<DepositResponse> findAllDeposits();
    List<DepositResponse> findDepositByAuctionSessionId(String auctionSessionId);
    List<DepositResponse> findDepositByUserId(String userId);
    BigDecimal maxDepositPriceByAuctionSessionId(String auctionSessionId);
    List<UsersJoinSessionResponse> getSessionsJoinByUserId(String userId);
}
