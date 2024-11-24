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
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
    SimpMessagingTemplate simpleMessageTemplate;

    //create AuctionHistory
    @Override
    @Transactional
    public AuctionHistoryResponse createAuctionHistory(AuctionHistoryCreateRequest request) {
        try {
            AuctionSession auctionSession = auctionSessionRepository.findById(request.getAuctionSessionId())
                    .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
            BigDecimal maxBidPrice = auctionHistoryRepository.findMaxBidPriceByAuctionSessionId(request.getAuctionSessionId());

            if (maxBidPrice == null) {
                maxBidPrice = auctionSession.getStartingBids();
            }

            if (request.getBidPrice().compareTo(maxBidPrice) <= 0) {
                throw new AppException(ErrorCode.BID_PRICE_MUST_GREATER_THAN_MAX_BID_PRICE);
            }
            var auctionHistory = auctionHistoryMapper.toAuctionHistory(request);
            setAuctionHistoryReference(request, auctionHistory);

            AuctionHistoryResponse auctionHistoryResponse = auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));

            AuctionSessionInfoResponse auctionSessionInfoResponse = auctionHistoryRepository.findAuctionSessionInfo(request.getAuctionSessionId());
            simpleMessageTemplate.convertAndSend("/rt-product/bidPrice-update", auctionSessionInfoResponse);
            return auctionHistoryResponse;
        } catch (OptimisticLockException e) {
            throw new AppException(ErrorCode.CONCURRENT_UPDATE);
        }
    }

    public Boolean checkDeposit(String userId, String auctionSessionId) {
        return auctionHistoryRepository.findAuctionHistoryByAuctionSession_AuctionSessionIdAndUser_UserId(auctionSessionId, userId) != null;
    }

    //Update AuctionHistory
    public AuctionHistoryResponse updateAuctionHistory(String id, AuctionHistoryUpdateRequest request) {
        AuctionHistory auctionHistory = auctionHistoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_HISTORY_NOT_EXISTED));
        auctionHistoryMapper.updateAuctionHistoryFromRequest(auctionHistory, request);
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));
    }

    public AuctionSessionInfoResponse getAuctionSessionInfo(String auctionSessionId) {
        return auctionHistoryRepository.findAuctionSessionInfo(auctionSessionId);
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
