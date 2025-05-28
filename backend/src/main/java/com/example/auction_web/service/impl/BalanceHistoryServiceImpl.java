package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BalanceHistoryCreateRequest;
import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.response.BalanceHistoryResponse;
import com.example.auction_web.dto.response.BalanceUserResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.BalanceUser;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.SessionWinner;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ACTIONBALANCE;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.enums.SESSION_WIN_STATUS;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.*;
import com.example.auction_web.repository.*;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.BalanceHistoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.auction_web.utils.TransactionCodeGenerator.generateTransactionCode;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class BalanceHistoryServiceImpl implements BalanceHistoryService {
    @NonFinal
    @Value("${email.username}")
    private String EMAIL_ADMIN;

    BalanceHistoryRepository balanceHistoryRepository;
    BalanceUserRepository balanceUserRepository;
    AuctionHistoryRepository auctionHistoryRepository;
    UserRepository userRepository;

    BalanceHistoryMapper balanceHistoryMapper;
    BalanceUserMapper balanceUserMapper;
    AuctionSessionMapper auctionSessionMapper;
    UserMapper userMapper;
    BillMapper billMapper;
    BillRepository billRepository;

    AuctionSessionRepository auctionSessionRepository;
    SessionWinnerRepository sessionWinnerRepository;
    AssetRepository assetRepository;

    private static final BigDecimal TEN_MILLION = new BigDecimal("10000000");
    private static final BigDecimal HUNDRED_MILLION = new BigDecimal("100000000");
    private static final BigDecimal ONE_BILLION = new BigDecimal("1000000000");

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

    // Khi người mua thanh toán phiên đấu giá
    @Transactional
    public void paymentSession(String buyerId, String sellerId, String sessionId) {
        var pricePayment = auctionHistoryRepository.findMaxBidPriceByAuctionSessionId(sessionId);
        var balanceBuyer = balanceUserMapper.toBalanceUserResponse(balanceUserRepository.findBalanceUserByUser_UserId(buyerId));
        var balanceSeller = balanceUserMapper.toBalanceUserResponse(balanceUserRepository.findBalanceUserByUser_UserId(sellerId));
        var auctionSession = auctionSessionMapper.toAuctionItemResponse(auctionSessionRepository.findById(sessionId).get());
        var admin = userMapper.toUserResponse(userRepository.findByEmail(EMAIL_ADMIN).get());
        var adminBalance = balanceUserMapper.toBalanceUserResponse(balanceUserRepository.findBalanceUserByUser_UserId(admin.getUserId()));

        if (balanceBuyer.getAccountBalance().compareTo(pricePayment) < 0) {
            throw new AppException(ErrorCode.BALANCE_NOT_ENOUGH);
        }

        BigDecimal commissionPercent = new BigDecimal(calculateCommission(pricePayment));
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal depositAmount = auctionSession.getDepositAmount();

        BigDecimal priceRemaining = pricePayment.subtract(depositAmount);
        BigDecimal priceCommission = pricePayment.multiply(commissionPercent).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal sellerReceive = pricePayment.subtract(priceCommission);

        balanceUserRepository.minusBalance(balanceBuyer.getBalanceUserId(), priceRemaining);
        addBalanceHistory(balanceBuyer.getBalanceUserId(), priceRemaining, "Thanh toán phiên " + sessionId, ACTIONBALANCE.SUBTRACT);

        // 2025/05/28 Bao Delete Start
        // balanceUserRepository.increaseBalance(adminBalance.getBalanceUserId(), priceCommission);
        // balanceUserRepository.minusBalance(adminBalance.getBalanceUserId(), depositAmount);
        // addBalanceHistory(adminBalance.getBalanceUserId(), priceCommission, "Hoa hồng phiên " + sessionId, ACTIONBALANCE.ADD);

        // balanceUserRepository.increaseBalance(balanceSeller.getBalanceUserId(), sellerReceive);
        // addBalanceHistory(balanceSeller.getBalanceUserId(), sellerReceive, "Nhận thanh toán phiên " + sessionId, ACTIONBALANCE.ADD);
        // 2025/05/28 Bao Delete End

        SessionWinner sessionWinner = sessionWinnerRepository.findByAuctionSession_AuctionSessionId(sessionId);
        if (sessionWinner != null) {
            sessionWinner.setStatus(SESSION_WIN_STATUS.PAYMENT_SUCCESSFUL.toString());
            sessionWinnerRepository.save(sessionWinner);
        }

        // Create bill
        var billRequest = BillCreateRequest.builder()
                .transactionCode(generateTransactionCode())
                .sessionId(sessionWinner.getAuctionSession().getAuctionSessionId())
                .buyerId(buyerId)
                .sellerId(sellerId)
                .totalPrice(pricePayment)
                .bidPrice(priceRemaining)
                .depositPrice(auctionSession.getDepositAmount())
                .billDate(LocalDateTime.now())
                .build();

        // 2025/05/28 Bao Add Start
        // Update Asset status
        AuctionSession auctionSessionEntity = auctionSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        if (auctionSessionEntity.getAsset() != null) {
            Asset asset = auctionSessionEntity.getAsset();
            asset.setStatus(ASSET_STATUS.PAYMENT_SUCCESSFUL.toString());
            asset.setUpdatedAt(LocalDateTime.now());
            assetRepository.save(asset);
        }
        // 2025/05/28 Bao Add End

        var bill = billMapper.toBill(billRequest);
        setBillReference(bill, billRequest);
        billRepository.save(bill);
    }

    // Khi người mua xác nhận đã nhận hàng thành công
    @Transactional
    public void comletedPaymentSession(String buyerId, String sellerId, String sessionId) {
        var pricePayment = auctionHistoryRepository.findMaxBidPriceByAuctionSessionId(sessionId);
        var balanceSeller = balanceUserMapper.toBalanceUserResponse(balanceUserRepository.findBalanceUserByUser_UserId(sellerId));
        var auctionSession = auctionSessionMapper.toAuctionItemResponse(auctionSessionRepository.findById(sessionId).get());
        var admin = userMapper.toUserResponse(userRepository.findByEmail(EMAIL_ADMIN).get());
        var adminBalance = balanceUserMapper.toBalanceUserResponse(balanceUserRepository.findBalanceUserByUser_UserId(admin.getUserId()));

        BigDecimal commissionPercent = new BigDecimal(calculateCommission(pricePayment));
        BigDecimal hundred = new BigDecimal("100");
        BigDecimal depositAmount = auctionSession.getDepositAmount();

        BigDecimal priceCommission = pricePayment.multiply(commissionPercent).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal sellerReceive = pricePayment.subtract(priceCommission);

        balanceUserRepository.increaseBalance(adminBalance.getBalanceUserId(), priceCommission);
        balanceUserRepository.minusBalance(adminBalance.getBalanceUserId(), depositAmount);
        addBalanceHistory(adminBalance.getBalanceUserId(), priceCommission, "Hoa hồng phiên " + sessionId, ACTIONBALANCE.ADD);

        balanceUserRepository.increaseBalance(balanceSeller.getBalanceUserId(), sellerReceive);
        addBalanceHistory(balanceSeller.getBalanceUserId(), sellerReceive, "Nhận thanh toán phiên " + sessionId, ACTIONBALANCE.ADD);

        SessionWinner sessionWinner = sessionWinnerRepository.findByAuctionSession_AuctionSessionId(sessionId);
        if (sessionWinner != null) {
            sessionWinner.setStatus(SESSION_WIN_STATUS.RECEIVED.toString());
            sessionWinnerRepository.save(sessionWinner);
        }

        AuctionSession auctionSessionEntity = auctionSessionRepository.findById(sessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        if (auctionSessionEntity.getAsset() != null) {
            Asset asset = auctionSessionEntity.getAsset();
            asset.setStatus(ASSET_STATUS.COMPLETED.toString());
            asset.setUpdatedAt(LocalDateTime.now());
            assetRepository.save(asset);
        }
    }

    public void cancelSession(String sellerId, String sessionId) {
        var auctionSession = auctionSessionRepository.findById(sessionId).orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        var admin = userRepository.findByEmail(EMAIL_ADMIN).orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        var adminBalance = balanceUserRepository.findBalanceUserByUser_UserId(admin.getUserId());

        BigDecimal depositAmount = auctionSession.getDepositAmount();
        BigDecimal commissionPercent = new BigDecimal(calculateCommission(depositAmount));
        BigDecimal hundred = new BigDecimal("100");

        BigDecimal priceCommission = depositAmount.multiply(commissionPercent).divide(hundred, 2, RoundingMode.HALF_UP);
        BigDecimal sellerReceive = depositAmount.subtract(priceCommission);

        balanceUserRepository.increaseBalance(adminBalance.getBalanceUserId(), priceCommission);
        addBalanceHistory(adminBalance.getBalanceUserId(), priceCommission, "Hoa hồng phiên " + sessionId, ACTIONBALANCE.ADD);

        balanceUserRepository.minusBalance(adminBalance.getBalanceUserId(), sellerReceive);
        addBalanceHistory(adminBalance.getBalanceUserId(), sellerReceive, "Thanh toán hủy thanh toán cho phiên " + sessionId, ACTIONBALANCE.SUBTRACT);

        balanceUserRepository.increaseBalance(sellerId, sellerReceive);
        addBalanceHistory(sellerId, sellerReceive, "Nhận thanh toán hủy thanh toán phiên " + sessionId, ACTIONBALANCE.ADD);
    }

    void addBalanceHistory(String BalanceUserId, BigDecimal amount, String Description, ACTIONBALANCE action) {
        var balanceUser = balanceUserRepository.findById(BalanceUserId)
                .orElseThrow(() -> new AppException(ErrorCode.BALANCE_USER_NOT_EXISTED));
        BalanceHistoryCreateRequest request = BalanceHistoryCreateRequest.builder()
                .amount(amount)
                .description(Description)
                .actionbalance(action)
                .build();
        var balanceHistory = balanceHistoryMapper.toBalanceHistory(request);
        balanceHistory.setBalanceUser(balanceUser);
        balanceHistoryRepository.save(balanceHistory);
    }

    public int calculateCommission(BigDecimal pricePayment) {
        if (pricePayment.compareTo(TEN_MILLION) < 0) {
            return 8;
        } else if (pricePayment.compareTo(HUNDRED_MILLION) < 0) {
            return 5;
        } else if (pricePayment.compareTo(ONE_BILLION) < 0) {
            return 3;
        } else {
            return 2;
        }
    }

    void setBillReference(Bill bill, BillCreateRequest request) {
        bill.setSession(getAuctionSession(request.getSessionId()));
        bill.setBuyerBill(getUser(request.getBuyerId()));
        bill.setSellerBill(getUser(request.getSellerId()));
    }

    AuctionSession getAuctionSession(String auctionSession) {
        return auctionSessionRepository.findById(auctionSession)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }

    User getUser(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
