package com.example.auction_web.service.impl;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.DepositUpdateRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.DepositResponse;
import com.example.auction_web.dto.response.UsersJoinSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ACTIONBALANCE;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.mapper.BalanceHistoryMapper;
import com.example.auction_web.mapper.DepositMapper;
import com.example.auction_web.repository.*;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.DepositService;
import com.example.auction_web.service.auth.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class DepositServiceImpl implements DepositService {
    DepositRepository depositRepository;
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    BalanceUserRepository balanceUserRepository;
    BalanceHistoryMapper balanceHistoryMapper;
    DepositMapper depositMapper;
    AuctionSessionMapper auctionSessionMapper;
    BalanceHistoryRepository balanceHistoryRepository;
    NotificationStompService notificationStompService;
    AuctionHistoryRepository auctionHistoryRepository;
    UserService userService;

    @NonFinal
    @Value("${email.username}")
    String EMAIL_ADMIN;

    // create a deposit
    @Transactional
    public DepositResponse createDeposit(DepositCreateRequest request) {

        if (request.getUserId() == null) {
            String userId = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            request.setUserId(userId);
        }
        if (request.getDepositPrice() == null) {
            var auctionSession = auctionSessionRepository.getById(request.getAuctionSessionId());
            request.setDepositPrice(auctionSession.getDepositAmount());
        }

        var checkDeposit = depositRepository.findByAuctionSession_AuctionSessionIdAndUser_UserId(request.getAuctionSessionId(), request.getUserId());
        if (checkDeposit != null) {
            throw new AppException(ErrorCode.DEPOSIT_IS_EXISTED);
        }

        if (request.getUserConfirmation() != null && !request.getUserConfirmation()) {
            var auctionSession = auctionSessionRepository.getById(request.getAuctionSessionId());
            throw new AppException(ErrorCode.USER_NOT_CONFIRMATION,
                    "Please confirm the information before making a deposit: SessionId: "
                            + request.getAuctionSessionId()
                            + " Session Name: " + auctionSession.getName()
                            + " Deposit Amount: " + request.getDepositPrice());
        }

        var deposit = depositMapper.toDeposit(request);
        var admin = userRepository.findByEmail(EMAIL_ADMIN)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        BalanceUser balanceUser = balanceUserRepository.findBalanceUserByUser_UserId(request.getUserId());
        if (balanceUser == null) {
            throw new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED);
        }
        if (balanceUser.getAccountBalance().compareTo(request.getDepositPrice()) < 0) {
            throw new AppException(ErrorCode.BALANCE_NOT_ENOUGH);
        }

        balanceUserRepository.minusBalance(request.getUserId(), request.getDepositPrice());
        addBalanceHistory(balanceUser.getBalanceUserId(), request.getDepositPrice(), "Deposit for auctionSessionId: " + request.getAuctionSessionId(), ACTIONBALANCE.SUBTRACT);
        balanceUserRepository.increaseBalance(admin.getUserId(), request.getDepositPrice());
        addBalanceHistory(balanceUserRepository.findBalanceUserByUser_Email(EMAIL_ADMIN).getBalanceUserId(), request.getDepositPrice(), "Deposit for auctionSessionId: " + request.getAuctionSessionId(), ACTIONBALANCE.ADD);

        setDepositReference(deposit, request);
        
        setDepositReference(deposit, request);
        Deposit savedDeposit = depositRepository.save(deposit);

        // Gửi thông báo đến người dùng
        NotificationRequest notification = NotificationRequest.builder()
            .senderId(userService.getUserByEmail(EMAIL_ADMIN).getUserId())
            .receiverId(request.getUserId())
            .type(NotificationType.DEPOSIT)
            .title("Đặt cọc thành công")
            .content("Bạn đã đặt cọc thành công " + request.getDepositPrice() + " VNĐ cho phiên: " + savedDeposit.getAuctionSession().getName())
            .referenceId(savedDeposit.getDepositId())
            .build();

        notificationStompService.sendUserNotification(request.getUserId(), notification);

        return depositMapper.toDepositResponse(savedDeposit);
    }   

    // update a deposit
    public DepositResponse updateDeposit(String id, DepositUpdateRequest request) {
        Deposit deposit = depositRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.DEPOSIT_NOT_EXISTED));
        depositMapper.updateDeposit(deposit, request);
        return depositMapper.toDepositResponse(depositRepository.save(deposit));
    }

    public Boolean checkDeposit(String auctionSessionId, String userId) {
        return depositRepository.findByAuctionSession_AuctionSessionIdAndUser_UserId(auctionSessionId, userId) != null;
    }

    // find all deposits
    public List<DepositResponse> findAllDeposits() {
        return depositRepository.findAll().stream()
                .map(depositMapper::toDepositResponse)
                .toList();
    }

    // find deposits by auction item id
    public List<DepositResponse> findDepositByAuctionSessionId(String auctionSession) {
        if (!auctionSessionRepository.existsById(auctionSession)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }
        return depositRepository.findDepositsByAuctionSession_AuctionSessionId(auctionSession).stream()
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

    public List<UsersJoinSessionResponse> getSessionsJoinByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return depositRepository.findSessionsJoinByUserId(userId).stream()
                .map(usersJoinSessionResponse -> {
                    var auctionSession = auctionSessionRepository.findById(usersJoinSessionResponse.getSessionId())
                            .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
                    usersJoinSessionResponse.setAuctionSession(auctionSessionMapper.toAuctionItemResponse(auctionSessionRepository.findById(usersJoinSessionResponse.getSessionId()).orElseThrow()));
                    var auctionSessionInfos = auctionHistoryRepository.findAuctionSessionInfo(usersJoinSessionResponse.getSessionId());
                    if (!auctionSessionInfos.isEmpty()) {
                        usersJoinSessionResponse.getAuctionSession().setAuctionSessionInfo(auctionSessionInfos.get(0));
                    }
                    return usersJoinSessionResponse;
                })
                .toList();
    }

    public BigDecimal maxDepositPriceByAuctionSessionId(String auctionSessionId) {
        return depositRepository.findMaxDepositPriceByAuctionSessionId(auctionSessionId);
    }

    // set deposit reference
    void setDepositReference(Deposit deposit, DepositCreateRequest request) {
        deposit.setAuctionSession(getAuctionSession(request.getAuctionSessionId()));
        deposit.setUser(getUser(request.getUserId()));
    }

    // get auction session
    AuctionSession getAuctionSession(String auctionSession) {
        return auctionSessionRepository.findById(auctionSession)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }

    // get user
    User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    void addBalanceHistory(String BalanceUserId, BigDecimal amount, String Description, ACTIONBALANCE action) {
        var balanceUser = balanceUserRepository.findById(BalanceUserId)
                .orElseThrow(() -> new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED));
        BalanceHistoryCreateRequest request = BalanceHistoryCreateRequest.builder()
                .balanceUserId(BalanceUserId)
                .amount(amount)
                .description(Description)
                .actionbalance(action)
                .build();
        var balanceHistory = balanceHistoryMapper.toBalanceHistory(request);
        balanceHistory.setBalanceUser(balanceUser);
        balanceHistoryRepository.save(balanceHistory);
    }
}
