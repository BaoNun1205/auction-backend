package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.request.AuctionSessionInfoRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionHistoryMapper;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuctionHistoryServiceImpl implements AuctionHistoryService {
    // init
    AuctionHistoryRepository auctionHistoryRepository;
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    AuctionHistoryMapper auctionHistoryMapper;

    //create AuctionHistory
    public AuctionHistoryResponse createAuctionHistory(AuctionHistoryCreateRequest request) {
        var auctionHistory = auctionHistoryMapper.toAuctionHistory(request);
        setAuctionHistoryReference(request, auctionHistory);
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));
    }

    //Update AuctionHistory
    public AuctionHistoryResponse updateAuctionHistory(String id, AuctionHistoryUpdateRequest request) {
        AuctionHistory auctionHistory = auctionHistoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_HISTORY_NOT_EXISTED));
        auctionHistoryMapper.updateAuctionHistoryFromRequest(auctionHistory, request);
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));
    }

    public AuctionSessionInfoResponse getAuctionSessionInfo(String auctionSessionId) {
        var totalAuctionHistoriesByAuctionSessionId = auctionHistoryRepository.countAuctionHistoriesByAuctionSession_AuctionSessionId(auctionSessionId);
        var highestBidPriceByAuctionSessionId = auctionHistoryRepository.findMaxBidPriceByAuctionSession(auctionSessionId);

        return AuctionSessionInfoResponse.builder()
                .totalAuctionHistory(totalAuctionHistoriesByAuctionSessionId)
                .highestBid(highestBidPriceByAuctionSessionId)
                .build();
    }

    // get all AuctionHistories
    public List<AuctionHistoryResponse> getAllAuctionHistories() {
        return auctionHistoryRepository.findAll().stream()
                .map(auctionHistoryMapper::toAuctionHistoryResponse)
                .toList();
    }

    //get AuctionHistories by AuctionItemId
    public AuctionHistoryResponse getAuctionHistoriesByAuctionSessionId(String auctionSessionId) {
        if (!auctionSessionRepository.existsById(auctionSessionId)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.findAuctionHistoryByAuctionSession_AuctionSessionId(auctionSessionId));
    }

    //set AuctionItem for AuctionHistory
    void setAuctionHistoryReference(AuctionHistoryCreateRequest request, AuctionHistory auctionHistory) {
        auctionHistory.setAuctionSession(getAuctionSessionById(request.getAuctionSessionId()));
        auctionHistory.setUser(getUserById(request.getUserId()));
    }

    //get AuctionItem by AuctionItemId
    AuctionSession getAuctionSessionById(String auctionSessionId) {
        return auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }

    User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
