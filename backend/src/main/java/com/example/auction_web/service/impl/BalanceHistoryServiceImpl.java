package com.example.auction_web.service.impl;

import com.example.auction_web.dto.response.BalanceHistoryResponse;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.repository.BalanceHistoryRepository;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.BalanceHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BalanceHistoryServiceImpl implements BalanceHistoryService {
    BalanceHistoryRepository balanceHistoryRepository;
    BalanceUserRepository balanceUserRepository;
    UserRepository userRepository;
    BalanceHistoryMapper balanceHistoryMapper;

    @Override
    public List<BalanceHistoryResponse> getAllBalanceHistoriesByBalanceUserId(String balanceUserId) {
        BalanceUser balanceUser = balanceUserRepository.findById(balanceUserId)
                .orElseThrow(() -> new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED));
        return balanceHistoryRepository.findBalanceHistoriesByBalanceUser_BalanceUserId(balanceUser.getBalanceUserId()).stream()
                .map(balanceHistoryMapper::toBalanceHistoryResponse)
                .toList();
    }

    @Override
    public List<BalanceHistoryResponse> getAllBalanceHistoriesByUserId(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return balanceHistoryRepository.findBalanceHistoriesByBalanceUser_User_UserId(user.getUserId()).stream()
                .map(balanceHistoryMapper::toBalanceHistoryResponse)
                .toList();
    }
}
