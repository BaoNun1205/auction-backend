package com.example.auction_web.service;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.auth.User;

import java.util.List;

public interface DepositService {
    DepositResponse createDeposit(DepositCreateRequest request);
    DepositResponse updateDeposit(String Id, DepositUpdateRequest request);
    List<DepositResponse> findAllDeposits();
    List<DepositResponse> findDepositByAuctionItem(AuctionItem auctionItem);
    List<DepositResponse> findDepositByUser(User user);
}
