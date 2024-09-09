package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.RegisterAuctionCreateRequest;
import com.example.auction_web.dto.request.RegisterAuctionUpdateRequest;
import com.example.auction_web.dto.response.RegisterAuctionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.RegisterAuction;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.RegisterAuctionMapper;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.RegisterAuctionRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.RegisterAuctionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class RegisterAuctionServiceImpl implements RegisterAuctionService {
    RegisterAuctionRepository registerAuctionRepository;
    UserRepository userRepository;
    AuctionSessionRepository auctionSessionRepository;
    RegisterAuctionMapper registerAuctionMapper;

    public RegisterAuctionResponse createRegisterAuction(RegisterAuctionCreateRequest request) {
        var registerAuction = registerAuctionMapper.toRegisterAuction(request);
        setReference(registerAuction, request);
        return registerAuctionMapper.toRegisterAuctionResponse(registerAuctionRepository.save(registerAuction));
    }

    public RegisterAuctionResponse updateRegisterAuction(String id, RegisterAuctionUpdateRequest request) {
        var registerAuction = registerAuctionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.REGISTER_AUCTION_NOT_EXISTED));
        registerAuctionMapper.updateRegisterAuction(registerAuction, request);
        return registerAuctionMapper.toRegisterAuctionResponse(registerAuctionRepository.save(registerAuction));
    }

    public List<RegisterAuctionResponse> findAllRegisterAuctions() {
        return registerAuctionRepository.findAll().stream()
                .map(registerAuctionMapper::toRegisterAuctionResponse)
                .toList();
    }

    public List<RegisterAuctionResponse> findRegisterAuctionByUserId(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
        return registerAuctionRepository.findRegisterAuctionsByUser_UserId(userId).stream()
                .map(registerAuctionMapper::toRegisterAuctionResponse)
                .toList();
    }

    public RegisterAuctionResponse findRegisterAuctionById(String id) {
        if (!registerAuctionRepository.existsById(id)) {
            throw new AppException(ErrorCode.REGISTER_AUCTION_NOT_EXISTED);
        }
        return registerAuctionRepository.findById(id)
                .map(registerAuctionMapper::toRegisterAuctionResponse)
                .orElseThrow(() -> new AppException(ErrorCode.REGISTER_AUCTION_NOT_EXISTED));
    }

    public RegisterAuctionResponse findRegisterAuctionByAuctionSessionId(String auctionSessionId) {
        if (!auctionSessionRepository.existsById(auctionSessionId)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }
        return registerAuctionMapper.toRegisterAuctionResponse(registerAuctionRepository.findRegisterAuctionByAuctionSession_AuctionSessionId(auctionSessionId));
    }

    void setReference(RegisterAuction registerAuction, RegisterAuctionCreateRequest request) {
        registerAuction.setUser(getUserById(request.getUserId()));
        registerAuction.setAuctionSession(getAuctionSessionById(request.getAuctionSessionId()));
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
