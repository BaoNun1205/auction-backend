package com.example.auction_web.service.impl;

import com.example.auction_web.dto.response.BalanceHistoryResponse;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
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
    AuctionHistoryRepository auctionHistoryRepository;
    UserRepository userRepository;

    BalanceHistoryMapper balanceHistoryMapper;
    AuctionSessionRepository auctionSessionRepository;

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
        return balanceHistoryRepository.findBalanceHistoriesByBalanceUser_User_UserIdOrderByCreatedAtDesc(user.getUserId()).stream()
                .map(balanceHistoryMapper::toBalanceHistoryResponse)
                .toList();
    }

    public void paymentSession(String buyerId, String sellerId, String sessionId) {
        var PricePayment = auctionHistoryRepository.findMaxBidPriceByAuctionSessionId(sessionId);
        var balanceBuyer = balanceUserRepository.findById(buyerId);
        var auctionSession = auctionSessionRepository.findById(sessionId);

        if (balanceBuyer.get().getAccountBalance().compareTo(PricePayment) < 0) {
            throw new AppException(ErrorCode.BALANCE_NOT_ENOUGH);
        } else {
            var PriceMin = PricePayment.min(auctionSession.get().getDepositAmount());
            balanceUserRepository.minusBalance(buyerId, PriceMin);
        }

        balanceUserRepository.increaseBalance(sellerId, PricePayment);
    }
}
