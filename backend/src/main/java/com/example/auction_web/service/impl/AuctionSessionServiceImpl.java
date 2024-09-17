package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Event;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.EventRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionSessionService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionServiceImpl implements AuctionSessionService {
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    AssetRepository assetRepository;
    AuctionSessionMapper auctionSessionMapper;

    public AuctionSessionResponse createAuctionSession(AuctionSessionCreateRequest request) {
        var auctionSession = auctionSessionMapper.toAuctionItem(request);
        setAuctionSessionReference(request, auctionSession);
        return auctionSessionMapper.toAuctionItemResponse(auctionSessionRepository.save(auctionSession));
    }

    public AuctionSessionResponse updateAuctionSession(String id, AuctionSessionUpdateRequest request) {
        AuctionSession auctionSession = auctionSessionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        auctionSessionMapper.updateAuctionItem(auctionSession, request);
        return auctionSessionMapper.toAuctionItemResponse(auctionSessionRepository.save(auctionSession));
    }

    public List<AuctionSessionResponse> getAllAuctionSessions() {
        return auctionSessionRepository.findAll().stream()
                .map(auctionSessionMapper::toAuctionItemResponse)
                .toList();
    }

    private void setAuctionSessionReference(AuctionSessionCreateRequest request, AuctionSession auctionSession) {
        auctionSession.setUser(getUserById(request.getUserId()));
        auctionSession.setAsset(getAssetById(request.getAssetId()));
    }

    User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }

    Asset getAssetById(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
    }
}
