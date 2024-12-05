package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.DepositCreateRequest;
import com.example.auction_web.dto.request.SessionWinnerCreateRequest;
import com.example.auction_web.dto.response.SessionWinnerResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Deposit;
import com.example.auction_web.entity.SessionWinner;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AssetMapper;
import com.example.auction_web.mapper.SessionWinnerMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.SessionWinnerRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.SessionWinnerService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class SessionWinnerServiceImpl implements SessionWinnerService {
    SessionWinnerRepository sessionWinnerRepository;
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    SessionWinnerMapper sessionWinnerMapper;
    AssetMapper assetMapper;
    private final AssetRepository assetRepository;

    public SessionWinnerResponse createSessionWinner(SessionWinnerCreateRequest request) {
        var sessionWinner = sessionWinnerMapper.toSessionWinner(request);
        setSessionWinnerReference(sessionWinner, request);
        return sessionWinnerMapper.toSessionWinnerResponse(sessionWinnerRepository.save(sessionWinner));
    }

    public SessionWinnerResponse getSessionWinner(String userId) {
        return sessionWinnerMapper.toSessionWinnerResponse(sessionWinnerRepository.getSessionWinnerByUser_UserId(userId));
    }

    void setSessionWinnerReference(SessionWinner sessionWinner, SessionWinnerCreateRequest request) {
        sessionWinner.setAuctionSession(getAuctionSession(request.getAuctionSessionId()));
        sessionWinner.setUser(getUser(request.getUserId()));
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
}
