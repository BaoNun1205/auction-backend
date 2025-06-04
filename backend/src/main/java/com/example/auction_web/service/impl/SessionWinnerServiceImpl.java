package com.example.auction_web.service.impl;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.BillCreateRequest;
import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.AssetResponse;
import com.example.auction_web.dto.response.SessionWinnerResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Bill;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.SessionWinner;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.ASSET_STATUS;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.enums.SESSION_WIN_STATUS;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.mapper.BillMapper;
import com.example.auction_web.mapper.SessionWinnerMapper;
import com.example.auction_web.repository.*;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.SessionWinnerService;
import com.example.auction_web.service.auth.UserService;
import com.example.auction_web.utils.Quataz.PaymentCancelService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

import org.mapstruct.control.MappingControl.Use;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.auction_web.utils.TransactionCodeGenerator.generateTransactionCode;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class SessionWinnerServiceImpl implements SessionWinnerService {
    SessionWinnerRepository sessionWinnerRepository;
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    SessionWinnerMapper sessionWinnerMapper;
    AssetMapper assetMapper;
    DepositRepository depositRepository;
    BillMapper billMapper;
    BillRepository billRepository;
    AssetRepository assetRepository;
    NotificationStompService notificationService;
    UserService userService;
    private final PaymentCancelService paymentCancelService;

    @NonFinal
    @Value("${email.username}")
    String EMAIL_ADMIN;

    public SessionWinnerResponse createSessionWinner(SessionWinnerCreateRequest request) {
        var sessionWinner = sessionWinnerMapper.toSessionWinner(request);
        setSessionWinnerReference(sessionWinner, request);
        return sessionWinnerMapper.toSessionWinnerResponse(sessionWinnerRepository.save(sessionWinner));
    }

    public SessionWinnerResponse getSessionWinner(String userId) {
        return sessionWinnerMapper.toSessionWinnerResponse(sessionWinnerRepository.getSessionWinnerByUser_UserId(userId));
    }

    void setSessionWinnerReference(SessionWinner sessionWinner, SessionWinnerCreateRequest request) {
        var auctionSession = getAuctionSession(request.getAuctionSessionId());
        sessionWinner.setAuctionSession(auctionSession);
        sessionWinner.setUser(getUser(request.getUserId()));
        // paymentCancelService.setSchedulerPaymentCancel(auctionSession.getUser().getUserId(), auctionSession.getAuctionSessionId(), LocalDateTime.now().plusDays(3));
        paymentCancelService.setSchedulerPaymentCancel(
            auctionSession.getUser().getUserId(),
            auctionSession.getAuctionSessionId(),
            LocalDateTime.now().plusMinutes(5)
        );
    }

    public List<SessionWinnerResponse> getSessionsWinner(String userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return sessionWinnerRepository.getSessionWinnersByUser_UserId(userId).stream()
                .map(sessionWinner -> {
                    SessionWinnerResponse response = sessionWinnerMapper.toSessionWinnerResponse(sessionWinner);
                    if (sessionWinner.getAuctionSession().getAsset() != null) {
                        response.getAuctionSession().setAsset(assetMapper.toAssetResponse(
                                assetRepository.findById(sessionWinner.getAuctionSession().getAsset().getAssetId()).orElse(null)));
                    } else {
                        response.getAuctionSession().setAsset(null);
                    }
                    return response;
                })
                .toList();
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

    public SessionWinnerResponse getSessionWinnerByAuctionSessionId(String auctionSessionId) {
        SessionWinner sessionWinner = sessionWinnerRepository
                .findByAuctionSession_AuctionSessionId(auctionSessionId);

        if (sessionWinner == null) {
            throw new AppException(ErrorCode.SESSION_WINNER_NOT_FOUND);
        }

        SessionWinnerResponse response = sessionWinnerMapper.toSessionWinnerResponse(sessionWinner);

        if (sessionWinner.getAuctionSession().getAsset() != null) {
            response.getAuctionSession().setAsset(
                    assetMapper.toAssetResponse(
                            assetRepository.findById(sessionWinner.getAuctionSession().getAsset().getAssetId()).orElse(null)
                    )
            );
        } else {
            response.getAuctionSession().setAsset(null);
        }

        return response;
    }

    public SessionWinnerResponse updateSessionWinnerStatus(String sessionWinnerId, SESSION_WIN_STATUS status) {
        SessionWinner sessionWinner = sessionWinnerRepository.findById(sessionWinnerId)
                .orElseThrow(() -> new AppException(ErrorCode.SESSION_WINNER_NOT_FOUND));

        sessionWinner.setStatus(status.toString());
        sessionWinner.setUpdatedAt(LocalDateTime.now());

        AuctionSession auctionSession = sessionWinner.getAuctionSession();
        if (status == SESSION_WIN_STATUS.RECEIVED && auctionSession != null && auctionSession.getAsset() != null) {
            var asset = auctionSession.getAsset();
            asset.setStatus(ASSET_STATUS.COMPLETED.toString());
            asset.setUpdatedAt(LocalDateTime.now());
            assetRepository.save(asset);

            // Send notification to vendor
            NotificationRequest notificationRequest = NotificationRequest.builder()
                    .senderId(userService.getUserByEmail(EMAIL_ADMIN).getUserId())
                    .receiverId(asset.getVendor().getUserId())
                    .type(NotificationType.ORDER_CONFIRMED)
                    .title("Người mua đã xác nhận nhận hàng")
                    .content("Người mua đã xác nhận đã nhận \"" + asset.getAssetName() + "\" thành công.")
                    .referenceId(asset.getAssetId())
                    .build();
            notificationService.sendUserNotification(asset.getVendor().getUserId(), notificationRequest);
        }

        SessionWinner updatedSessionWinner = sessionWinnerRepository.save(sessionWinner);
        SessionWinnerResponse response = sessionWinnerMapper.toSessionWinnerResponse(updatedSessionWinner);

        if (updatedSessionWinner.getAuctionSession().getAsset() != null) {
            response.getAuctionSession().setAsset(
                    assetMapper.toAssetResponse(
                            assetRepository.findById(updatedSessionWinner.getAuctionSession().getAsset().getAssetId()).orElse(null)
                    )
            );
        } else {
            response.getAuctionSession().setAsset(null);
        }

        return response;
    }

    // Dùng đỡ để update status asset
    public AssetResponse updateAssetStatus(String assetId, ASSET_STATUS status) {
        Asset asset = assetRepository.findById(assetId)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));

        asset.setStatus(status.toString());
        asset.setUpdatedAt(LocalDateTime.now());

        // Handle SessionWinner and notification
        if (asset.getAuctionSession() != null) {
            SessionWinner sessionWinner = sessionWinnerRepository.findByAuctionSession_AuctionSessionId(
                    asset.getAuctionSession().getAuctionSessionId());
            if (sessionWinner != null) {
                if (status == ASSET_STATUS.DELIVERING) {
                    sessionWinner.setStatus(SESSION_WIN_STATUS.DELIVERING.toString());
                    sessionWinner.setUpdatedAt(LocalDateTime.now());
                    sessionWinnerRepository.save(sessionWinner);

                    // Send notification to user
                    NotificationRequest notificationRequest = NotificationRequest.builder()
                            .senderId(userService.getUserByEmail(EMAIL_ADMIN).getUserId())
                            .receiverId(sessionWinner.getUser().getUserId())
                            .type(NotificationType.ORDER_CONFIRMED)
                            .title("Vật phẩm đang được giao")
                            .content("Vui lòng xác nhận " + asset.getAssetName() + " khi nhận hàng thành công.")
                            .referenceId(assetId)
                            .build();
                    notificationService.sendUserNotification(sessionWinner.getUser().getUserId(), notificationRequest);
                } else if (status == ASSET_STATUS.RECEIVED) {
                    // Send notification to user
                    NotificationRequest notificationRequest = NotificationRequest.builder()
                            .senderId(userService.getUserByEmail(EMAIL_ADMIN).getUserId())
                            .receiverId(sessionWinner.getUser().getUserId())
                            .type(NotificationType.ORDER_CONFIRMED)
                            .title("Xác nhận đã nhận vật phẩm")
                            .content("Vui lòng xác nhận " + asset.getAssetName() + " đã được nhận thành công.")
                            .referenceId(assetId)
                            .build();
                    notificationService.sendUserNotification(sessionWinner.getUser().getUserId(), notificationRequest);
                }
            }
        }

        Asset updatedAsset = assetRepository.save(asset);
        return assetMapper.toAssetResponse(updatedAsset);
    }
}
