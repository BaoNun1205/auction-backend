package com.example.auction_web.service.impl;

import com.example.auction_web.WebSocket.service.NotificationStompService;
import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.request.notification.NotificationRequest;
import com.example.auction_web.dto.response.RegisterSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.RegisterSession;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.NotificationType;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.mapper.RegisterSessionMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.RegisterSessionRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.RegisterSessionService;
import com.example.auction_web.utils.Quataz.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RegisterSessionServiceImpl implements RegisterSessionService {
    RegisterSessionRepository registerSessionRepository;
    UserRepository userRepository;
    AuctionSessionRepository auctionSessionRepository;
    AssetRepository assetRepository;
    AssetMapper assetMapper;
    RegisterSessionMapper registerSessionMapper;
    NotificationService notificationService;
    NotificationStompService notificationStompService;

    @Override
    public RegisterSessionResponse createRegisterSession(RegisterSessionCreateRequest request) {
        try {
            var registerSession = registerSessionRepository
                    .findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionIdAndDelFlagIsTrue(
                            request.getUserId(), request.getAuctionSessionId());
    
            boolean isNewRegister = false;
    
            if (registerSession == null) {
                registerSession = registerSessionMapper.toRegisterSession(request);
                setRegisterReference(request, registerSession);
            } else {
                registerSession.setDelFlag(false);
                isNewRegister = true;
            }
    
            var user = getUserById(request.getUserId());
            var auctionSession = getAuctionSessionById(request.getAuctionSessionId());
    
            // Lưu trước rồi mới gửi thông báo
            registerSession = registerSessionRepository.save(registerSession);
    
            // Gửi thông báo nếu là đăng ký mới
            if (isNewRegister) {
                NotificationRequest notificationRequest = NotificationRequest.builder()
                        .senderId(user.getUserId())
                        .receiverId(auctionSession.getUser().getUserId())
                        .title("Có người mới đăng ký phiên đấu giá")
                        .content(user.getUsername() + " vừa đăng ký phiên: " + auctionSession.getName())
                        .type(NotificationType.NEW_REGISTRATION)
                        .referenceId(auctionSession.getAuctionSessionId())
                        .build();
    
                notificationStompService.sendUserNotification(
                        auctionSession.getUser().getUserId(),
                        notificationRequest
                );
            }
    
            // Thiết lập thông báo trước giờ phiên đấu giá
            LocalDateTime notificationTime = auctionSession.getStartTime().minusMinutes(30);
            notificationService.setSchedulerNotification(
                    user.getEmail(), auctionSession.getAuctionSessionId(), notificationTime
            );
    
            return registerSessionMapper.toRegisterSessionResponse(registerSession);
    
        } catch (Exception e) {
            throw new AppException(ErrorCode.CREATE_REGISTER_SESSION_FAILED);
        }
    }    

    @Override
    public RegisterSessionResponse updateRegisterSession(String registerAuctionId, RegisterSessionCreateRequest request) {
        var registerSession = registerSessionRepository.findById(registerAuctionId)
                .orElseThrow(() -> new AppException(ErrorCode.REGISTER_SESSION_NOT_EXISTED));
        registerSessionMapper.updateRegisterSession(registerSession, request);
        setRegisterReference(request, registerSession);
        return registerSessionMapper.toRegisterSessionResponse(registerSessionRepository.save(registerSession));
    }

    @Override
    public void unRegisterSession(RegisterSessionCreateRequest request) {
        RegisterSession registerSession =
                registerSessionRepository.findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionIdAndDelFlagIsFalse(request.getUserId(), request.getAuctionSessionId());
        registerSession.setDelFlag(true);
        registerSessionRepository.save(registerSession);
    }

    @Override
    public List<RegisterSessionResponse> getRegisterSessions() {
        return registerSessionRepository.findAll().stream()
                .map(registerSessionMapper::toRegisterSessionResponse)
                .toList();
    }

    @Override
    public Boolean getRegisterSessionByUserAndAuctionSession(String userId, String auctionSessionId) {
        return registerSessionRepository.findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionIdAndDelFlagIsFalse(userId, auctionSessionId) != null;
    }

    @Override
    public List<RegisterSessionResponse> getRegisterSessionByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return registerSessionRepository.findRegisterSessionByUser_UserIdAndDelFlagIsFalse(userId).stream()
                .map(registerSession -> {
                    RegisterSessionResponse response = registerSessionMapper.toRegisterSessionResponse(registerSession);

                    if (registerSession.getAuctionSession().getAsset() != null) {
                        response.getAuctionSession().setAsset(assetMapper.toAssetResponse(
                                assetRepository.findById(registerSession.getAuctionSession().getAsset().getAssetId()).orElse(null)
                        ));
                    } else {
                        response.getAuctionSession().setAsset(null);
                    }
                    return response;
                })
                .toList();
    }

    @Override
    public List<RegisterSessionResponse> getRegisterSessionByAuctionSessionId(String auctionSessionId) {
        if (!auctionSessionRepository.existsById(auctionSessionId)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }
        return registerSessionRepository.findRegisterSessionByAuctionSession_AuctionSessionIdAndDelFlagIsFalse(auctionSessionId).stream()
                .map(registerSession -> {
                    RegisterSessionResponse response = registerSessionMapper.toRegisterSessionResponse(registerSession);

                    if (registerSession.getAuctionSession().getAsset() != null) {
                        response.getAuctionSession().setAsset(assetMapper.toAssetResponse(
                                assetRepository.findById(registerSession.getAuctionSession().getAsset().getAssetId()).orElse(null)
                        ));
                    } else {
                        response.getAuctionSession().setAsset(null);
                    }
                    return response;
                })
                .toList();
    }

    void setRegisterReference(RegisterSessionCreateRequest request, RegisterSession registerSession) {
        registerSession.setUser(getUserById(request.getUserId()));
        registerSession.setAuctionSession(getAuctionSessionById(request.getAuctionSessionId()));
    }

    User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    AuctionSession getAuctionSessionById(String auctionSessionId) {
        return auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }
}
