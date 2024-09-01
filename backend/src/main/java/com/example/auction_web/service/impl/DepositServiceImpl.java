package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.mapper.DepositMapper;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class DepositServiceImpl implements DepositService {
    DepositRepository depositRepository;
    DepositMapper depositMapper;

    public DepositResponse createDeposit(DepositCreateRequest request) {
        return depositMapper.toDepositResponse(depositRepository.save(depositMapper.toDeposit(request)));
    }

    public DepositResponse updateDeposit(String id, DepositUpdateRequest request) {
        Deposit deposit = depositRepository.findById(id).orElseThrow(() -> new RuntimeException("Not Found"));
        depositMapper.updateDeposit(deposit, request);
        return depositMapper.toDepositResponse(depositRepository.save(deposit));
    }

    public List<DepositResponse> findAllDeposits() {
        return depositRepository.findAll().stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    public List<DepositResponse> findDepositByAuctionItem(AuctionItem auctionItem) {
        return depositRepository.findDepositByAuctionItem(auctionItem).stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    public List<DepositResponse> findDepositByUser(User user) {
        return depositRepository.findDepositByUser(user).stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }
}
