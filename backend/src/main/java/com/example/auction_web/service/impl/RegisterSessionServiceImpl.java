package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.RegisterSessionCreateRequest;
import com.example.auction_web.dto.response.RegisterSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.RegisterSession;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.RegisterSessionMapper;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.RegisterSessionRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.RegisterSessionService;
import com.example.auction_web.utils.NotificationService;
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
    RegisterSessionMapper registerSessionMapper;
    NotificationService notificationService;

    @Override
    public RegisterSessionResponse createRegisterSession(RegisterSessionCreateRequest request) {
        var registerSession = registerSessionMapper.toRegisterSession(request);
        setRegisterReference(request, registerSession);
        var user = getUserById(request.getUserId());
        var auctionSession = getAuctionSessionById(request.getAuctionSessionId());

        LocalDateTime notificationTime = auctionSession.getStartTime().minusMinutes(30);
        notificationService.setSchedulerNotification(user.getEmail(), auctionSession.getAuctionSessionId(), notificationTime);

        return registerSessionMapper.toRegisterSessionResponse(registerSessionRepository.save(registerSession));
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
    public List<RegisterSessionResponse> getRegisterSessions() {
        return registerSessionRepository.findAll().stream()
                .map(registerSessionMapper::toRegisterSessionResponse)
                .toList();
    }

    @Override
    public RegisterSessionResponse getRegisterSessionByUserAndAuctionSession(String userId, String auctionSessionId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        if (!auctionSessionRepository.existsById(auctionSessionId)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }
        return registerSessionMapper.toRegisterSessionResponse(registerSessionRepository.findRegisterSessionByUser_UserIdAndAuctionSession_AuctionSessionId(userId, auctionSessionId));
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
