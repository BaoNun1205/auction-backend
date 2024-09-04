package com.example.auction_web.service;

import com.example.auction_web.dto.request.CoinUserCreateRequest;
import com.example.auction_web.dto.request.CoinUserUpdateRequest;
import com.example.auction_web.dto.response.CoinUserResponse;
import com.example.auction_web.entity.auth.User;

import java.util.List;

public interface CoinUserService {
    CoinUserResponse createCoinUser(CoinUserCreateRequest request);
    CoinUserResponse updateCoinUser(String Id, CoinUserUpdateRequest request);
    List<CoinUserResponse> getCoinUsers();
    CoinUserResponse getCoinUserByUserId(String userId);

}
