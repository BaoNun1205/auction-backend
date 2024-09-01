package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.CoinUserCreateRequest;
import com.example.auction_web.dto.request.CoinUserUpdateRequest;
import com.example.auction_web.dto.response.CoinUserResponse;
import com.example.auction_web.entity.CoinUser;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.mapper.CoinUserMapper;
import com.example.auction_web.repository.CoinUserRepository;
import com.example.auction_web.service.CoinUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class CoinUserServiceImpl implements CoinUserService {
    CoinUserRepository coinUserRepository;
    CoinUserMapper coinUserMapper;

    public CoinUserResponse createCoinUser(CoinUserCreateRequest request) {
        return coinUserMapper.toCoinUserResponse(coinUserRepository.save(coinUserMapper.toCoinUser(request)));
    }

    public CoinUserResponse updateCoinUser(String id, CoinUserUpdateRequest request) {
        CoinUser coinUser = coinUserRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        coinUserMapper.updateCoinUser(coinUser, request);
        return coinUserMapper.toCoinUserResponse(coinUserRepository.save(coinUser));
    }

    public List<CoinUserResponse> getCoinUsers() {
        return coinUserRepository.findAll().stream()
                .map(coinUserMapper::toCoinUserResponse)
                .toList();
    }

    public CoinUserResponse getCoinUserByUser(User user) {
        return coinUserMapper.toCoinUserResponse(coinUserRepository.findCoinUserByUser(user));
    }
}
