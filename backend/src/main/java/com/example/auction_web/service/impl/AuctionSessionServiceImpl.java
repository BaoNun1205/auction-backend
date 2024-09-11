package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionItemResponse;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.repository.AuctionSessionRepository;
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
    AuctionSessionMapper auctionSessionMapper;

    public AuctionSessionResponse createAuctionSession(AuctionSessionCreateRequest request) {
        return auctionSessionMapper.toAuctionSessionResponse(auctionSessionRepository.save(auctionSessionMapper.toAuctionSession(request)));
    }

    public AuctionSessionResponse updateAuctionSession(String id, AuctionSessionUpdateRequest request) {
        AuctionSession auctionSession = auctionSessionRepository.findById(id).orElseThrow();
        auctionSessionMapper.updateAuctionSession(auctionSession, request);
        return auctionSessionMapper.toAuctionSessionResponse(auctionSessionRepository.save(auctionSession));
    }

    public List<AuctionSessionResponse> getAllAuctionSessions() {
        return auctionSessionRepository.findAll().stream()
                .map(auctionSessionMapper::toAuctionSessionResponse)
                .toList();
    }

    public AuctionSessionResponse  getAuctionSessionById(String id) {
        var auctionSession = auctionSessionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        return auctionSessionMapper.toAuctionSessionResponse(auctionSession);
    }
}
