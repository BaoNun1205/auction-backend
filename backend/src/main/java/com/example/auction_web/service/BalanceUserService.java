package com.example.auction_web.service;

import com.example.auction_web.dto.request.BalanceUserCreateRequest;
import com.example.auction_web.dto.request.BalanceUserUpdateRequest;
import com.example.auction_web.dto.response.BalanceUserResponse;

import java.util.List;

public interface BalanceUserService {
    BalanceUserResponse createCoinUser(BalanceUserCreateRequest request);
    BalanceUserResponse updateCoinUser(String Id, BalanceUserUpdateRequest request);
    List<BalanceUserResponse> getCoinUsers();
    BalanceUserResponse getCoinUserByUserId(String userId);

}
