package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.SessionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionHistoryMapper;
import com.example.auction_web.mapper.UserMapper;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionHistoryService;
import com.example.auction_web.utils.RabbitMQ.BidEventProducer;
import com.example.auction_web.utils.RabbitMQ.Dto.BidMessage;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
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
    DepositRepository depositRepository;
    UserRepository userRepository;
    AuctionHistoryMapper auctionHistoryMapper;
    UserMapper userMapper;

    BidEventProducer bidEventProducer;

    //create AuctionHistory
    @Override
    @Transactional
    public AuctionHistoryResponse createAuctionHistory(AuctionHistoryCreateRequest request) {
        try {
            String userId = auctionHistoryRepository.findMaxUserBidPrice(request.getAuctionSessionId());
            Deposit deposit = depositRepository.findByAuctionSession_AuctionSessionIdAndUser_UserId(request.getAuctionSessionId(), request.getUserId());
            if (deposit == null) {
                throw new AppException(ErrorCode.DEPOSIT_NOT_EXISTED);
            }
            if (userId != null && userId.equals(request.getUserId())) {
                throw new AppException(ErrorCode.USER_CANNOT_BID_HIGHER_THAN_HIS_BID);
            }
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

            sendMessageToRabbitMQ(request.getAuctionSessionId(), request.getBidPrice());
            return auctionHistoryResponse;
        } catch (OptimisticLockException e) {
            throw new AppException(ErrorCode.CONCURRENT_UPDATE);
        }
    }

    //Update AuctionHistory
    public AuctionHistoryResponse updateAuctionHistory(String id, AuctionHistoryUpdateRequest request) {
        AuctionHistory auctionHistory = auctionHistoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_HISTORY_NOT_EXISTED));
        auctionHistoryMapper.updateAuctionHistoryFromRequest(auctionHistory, request);
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));
    }

    public AuctionSessionInfoResponse getAuctionSessionInfo(String auctionSessionId) {
        return auctionHistoryRepository.findAuctionSessionInfo(auctionSessionId).get(0);
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

    public List<SessionHistoryResponse> getSessionsHistoryByAuctionSessionId(String auctionSessionId) {
        if (!auctionSessionRepository.existsById(auctionSessionId)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }

        return auctionHistoryRepository.findSessionHistoryByAuctionSessionId(auctionSessionId).stream()
                .map(sessionHistoryResponse -> {
                    sessionHistoryResponse.setUser(userMapper.toUserResponse(userRepository.findById(sessionHistoryResponse.getUserId())
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED))));
                    return sessionHistoryResponse;
                })
                .toList();
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

    //Send Message to RabbitMQ
    void sendMessageToRabbitMQ(String auctionSessionId, BigDecimal currentPrice) {
        BidMessage bidMessage = BidMessage.builder()
                .auctionSessionId(auctionSessionId)
                .currentPrice(currentPrice)
                .build();
        bidEventProducer.sendBidEvent(bidMessage);
    }
}
