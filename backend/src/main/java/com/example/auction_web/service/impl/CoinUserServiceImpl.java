package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.CoinUserCreateRequest;
import com.example.auction_web.dto.request.CoinUserUpdateRequest;
import com.example.auction_web.dto.response.CoinUserResponse;
import com.example.auction_web.entity.CoinUser;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.CoinUserMapper;
import com.example.auction_web.repository.CoinUserRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.CoinUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CoinUserServiceImpl implements CoinUserService {
    CoinUserRepository coinUserRepository;
    UserRepository userRepository;
    CoinUserMapper coinUserMapper;

    public CoinUserResponse createCoinUser(CoinUserCreateRequest request) {
        var coinUser = coinUserMapper.toCoinUser(request);

        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        coinUser.setUser(user);
        return coinUserMapper.toCoinUserResponse(coinUserRepository.save(coinUser));
    }

    public CoinUserResponse updateCoinUser(String id, CoinUserUpdateRequest request) {
        CoinUser coinUser = coinUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.COIN_USER_NOT_EXISTED));
        coinUserMapper.updateCoinUser(coinUser, request);
        return coinUserMapper.toCoinUserResponse(coinUserRepository.save(coinUser));
    }

    public List<CoinUserResponse> getCoinUsers() {
        return coinUserRepository.findAll().stream()
                .map(coinUserMapper::toCoinUserResponse)
                .toList();
    }

    public CoinUserResponse getCoinUserByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return coinUserMapper.toCoinUserResponse(coinUserRepository.findCoinUserByUser_UserId(userId));
    }
}
