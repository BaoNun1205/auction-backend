package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.DepositMapper;
import com.example.auction_web.repository.AuctionItemRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.DepositService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DepositServiceImpl implements DepositService {
    DepositRepository depositRepository;
    AuctionItemRepository auctionItemRepository;
    UserRepository userRepository;
    DepositMapper depositMapper;

    // create a deposit
    public DepositResponse createDeposit(DepositCreateRequest request) {
        var deposit = depositMapper.toDeposit(request);
        setdepositRefernce(deposit, request);
        return depositMapper.toDepositResponse(depositRepository.save(deposit));
    }

    // update a deposit
    public DepositResponse updateDeposit(String id, DepositUpdateRequest request) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPOSIT_NOT_EXISTED));
        depositMapper.updateDeposit(deposit, request);
        return depositMapper.toDepositResponse(depositRepository.save(deposit));
    }

    // find all deposits
    public List<DepositResponse> findAllDeposits() {
        return depositRepository.findAll().stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    // find deposits by auction item id
    public List<DepositResponse> findDepositByAuctionItemId(String auctionItemId) {
        if (!auctionItemRepository.existsById(auctionItemId)) {
            throw new AppException(ErrorCode.AUCTION_ITEM_NOT_EXISTED);
        }
        return depositRepository.findDepositsByAuctionItem_AuctionItemId(auctionItemId).stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    //  find deposits by user id
    public List<DepositResponse> findDepositByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return depositRepository.findDepositsByUser_UserId(userId).stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    // set deposit reference
    void setdepositRefernce(Deposit deposit, DepositCreateRequest request) {
        deposit.setAuctionItem(getAuctionItem(request.getAuctionItemId()));
        deposit.setUser(getUser(request.getUserId()));
    }

    // get auction item
    AuctionItem getAuctionItem(String auctionItemId) {
        return auctionItemRepository.findById(auctionItemId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_ITEM_NOT_EXISTED));
    }

    // get user
    User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
