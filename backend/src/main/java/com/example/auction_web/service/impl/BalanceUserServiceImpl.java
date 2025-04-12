package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.dto.request.BalanceUserCreateRequest;
import com.example.auction_web.dto.request.BalanceUserUpdateRequest;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.enums.ACTIONBALANCE;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.mapper.BalanceUserMapper;
import com.example.auction_web.repository.BalanceHistoryRepository;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.BalanceUserService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BalanceUserServiceImpl implements BalanceUserService {
    BalanceUserRepository balanceUserRepository;
    UserRepository userRepository;
    BalanceHistoryRepository balanceHistoryRepository;
    BalanceHistoryMapper balanceHistoryMapper;
    BalanceUserMapper balanceUserMapper;

    public BalanceUserResponse createCoinUser(BalanceUserCreateRequest request) {
        var coinUser = balanceUserMapper.toBalanceUser(request);

        var user = userRepository.findById(request.getUserId()).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        coinUser.setUser(user);
        return balanceUserMapper.toBalanceUserResponse(balanceUserRepository.save(coinUser));
    }

    public BalanceUserResponse updateCoinUser(String id, BalanceUserUpdateRequest request) {
        BalanceUser balanceUser = balanceUserRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED));
        balanceUserMapper.updateBalance(balanceUser, request);
        return balanceUserMapper.toBalanceUserResponse(balanceUserRepository.save(balanceUser));
    }

    public List<BalanceUserResponse> getCoinUsers() {
        return balanceUserRepository.findAll().stream()
                .map(balanceUserMapper::toBalanceUserResponse)
                .toList();
    }

    public BalanceUserResponse getCoinUserByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return balanceUserMapper.toBalanceUserResponse(balanceUserRepository.findBalanceUserByUser_UserId(userId));
    }

    @Override
    public BalanceUserResponse updateCoinUserVnPay(String userId, String orderInfo, BigDecimal amount) {
        BalanceUser balanceUser = balanceUserRepository.findBalanceUserByUser_UserId(userId);
        if (balanceUser == null) {
            throw new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED);
        }
        balanceUser.setAccountBalance(balanceUser.getAccountBalance().add(amount));
        addBalanceHistory(balanceUser.getBalanceUserId(), amount, orderInfo);
        return balanceUserMapper.toBalanceUserResponse(balanceUserRepository.save(balanceUser));
    }

    void addBalanceHistory(String BalanceUserId, BigDecimal amount, String Description) {
        var balanceUser = balanceUserRepository.findById(BalanceUserId)
                .orElseThrow(() -> new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED));
        BalanceHistoryCreateRequest request = BalanceHistoryCreateRequest.builder()
                .balanceUserId(BalanceUserId)
                .amount(amount)
                .description(Description)
                .actionbalance(ACTIONBALANCE.ADD)
                .build();
        var balanceHistory = balanceHistoryMapper.toBalanceHistory(request);
        balanceHistory.setBalanceUser(balanceUser);
        balanceHistoryRepository.save(balanceHistory);
    }
}
