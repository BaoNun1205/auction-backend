package com.example.auction_web.service.impl;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.dto.response.AuctionSessionInfoResponse;
import com.example.auction_web.dto.response.SessionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionHistoryMapper;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.DepositRepository;
import com.example.auction_web.service.AuctionHistoryService;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.utils.RabbitMQ.Producer.BidEventProducer;
import com.example.auction_web.utils.RabbitMQ.Dto.BidMessage;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
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
    UserService userService;
    AuctionHistoryMapper auctionHistoryMapper;
    NotificationStompService notificationStompService;
    BidEventProducer bidEventProducer;

    //create AuctionHistory
    @Override
    @Transactional
    public AuctionHistoryResponse createAuctionHistory(AuctionHistoryCreateRequest request) {
        if (request.getUserId() == null) {
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            request.setUserId(userId);
        }
        try {
            List<String> userIds = auctionHistoryRepository
                    .findTopUserBidPriceByBidTime(request.getAuctionSessionId(), PageRequest.of(0, 1));

            String userId = (userIds != null && !userIds.isEmpty()) ? userIds.get(0) : null;

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

            // Thông báo đến những người tham gia đấu giá
            User sender = userService.getUser(request.getUserId());
            String senderDisplayName = sender.getName() != null ? sender.getName() : sender.getUsername();

            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .senderId(sender.getUserId())
                    .receiverId(null)
                    .type(NotificationType.NEW_BID)
                    .title("Có người vừa đặt giá mới!")
                    .content(senderDisplayName + " vừa đặt " + request.getBidPrice() + " cho phiên đấu giá.")
                    .referenceId(request.getAuctionSessionId())
                    .build();

            notificationStompService.sendNewBidNotification(request.getAuctionSessionId(), notificationRequest);

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
                    sessionHistoryResponse.setUser(userService.getUserResponse(sessionHistoryResponse.getUserId()));
                    return sessionHistoryResponse;
                })
                .toList();
    }

    //set AuctionItem for AuctionHistory
    void setAuctionHistoryReference(AuctionHistoryCreateRequest request, AuctionHistory auctionHistory) {
        auctionHistory.setAuctionSession(getAuctionSessionById(request.getAuctionSessionId()));
        auctionHistory.setUser(userService.getUser(request.getUserId()));
    }

    //get AuctionItem by AuctionItemId
    AuctionSession getAuctionSessionById(String auctionSessionId) {
        return auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
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
