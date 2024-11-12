package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionSessionCreateRequest;
import com.example.auction_web.dto.request.AuctionSessionUpdateRequest;
import com.example.auction_web.dto.response.AuctionSessionInfoDetail;
import com.example.auction_web.dto.response.AuctionSessionResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.entity.Event;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.enums.AUCTION_STATUS;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionSessionMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.repository.EventRepository;
import com.example.auction_web.repository.ImageAssetRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionSessionService;
import com.example.auction_web.service.ImageAssetService;
import com.example.auction_web.service.specification.AuctionSessionSpecification;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionSessionServiceImpl implements AuctionSessionService {
    AuctionSessionRepository auctionSessionRepository;
    UserRepository userRepository;
    AssetRepository assetRepository;
    AuctionSessionMapper auctionSessionMapper;
    ImageAssetService imageAssetService;

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

    public AuctionSessionInfoDetail getDetailAuctionSessionById(String auctionSessionId) {
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        AuctionSessionInfoDetail auctionSessionInfoDetail = auctionSessionRepository.findAuctionSessionInfoDetailById(auctionSession.getAuctionSessionId());
        auctionSessionInfoDetail.setListImage(imageAssetService.findAllImageAssetsByAssetId(auctionSession.getAsset().getAssetId()));
        return auctionSessionInfoDetail;
    }

    public List<AuctionSessionResponse> filterAuctionSession(String status, LocalDateTime fromDate, LocalDateTime toDate, String keyword, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        if (isAllParamsNullOrEmpty(status, fromDate, toDate, keyword)) {
            return auctionSessionRepository.findAll().stream()
                    .map(auctionSessionMapper::toAuctionItemResponse)
                    .toList();
        }

        Specification<AuctionSession> specification = Specification
                .where(AuctionSessionSpecification.hasStatus(status))
                .and(AuctionSessionSpecification.hasFromDateToDate(fromDate, toDate))
                .and(AuctionSessionSpecification.hasKeyword(keyword));

        return auctionSessionRepository.findAll(specification, pageable).stream()
                .map(auctionSessionMapper::toAuctionItemResponse)
                .toList();
    }

    public int totalAuctionSession(String status, LocalDateTime fromDate, LocalDateTime toDate, String keyword) {
        if (isAllParamsNullOrEmpty(status, fromDate, toDate, keyword)) {
            return auctionSessionRepository.findAll().size();
        }

        Specification<AuctionSession> specification = Specification
                .where(AuctionSessionSpecification.hasStatus(status))
                .and(AuctionSessionSpecification.hasFromDateToDate(fromDate, toDate))
                .and(AuctionSessionSpecification.hasKeyword(keyword));

        return auctionSessionRepository.findAll(specification).size();
    }

    public AuctionSessionResponse getAuctionSessionById(String auctionSessionId) {
        return auctionSessionMapper.toAuctionItemResponse(auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED)));
    }

    public List<AuctionSessionResponse> getListAuctionSessionByStatus(String status) {
        return auctionSessionRepository.findAuctionSessionByStatusOrderByStartTimeAsc(status).stream()
                .map(auctionSessionMapper::toAuctionItemResponse)
                .toList();
    }

    @Transactional
    public void completeAuctionSession(String auctionSessionId) {
        AuctionSession auctionSession = auctionSessionRepository.findById(auctionSessionId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
        auctionSession.setStatus(String.valueOf(AUCTION_STATUS.FINISHED));
        auctionSessionRepository.save(auctionSession);
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

    public boolean isAllParamsNullOrEmpty(String status, LocalDateTime fromDate, LocalDateTime toDate, String keyword) {
        return (status == null || status.isEmpty()) && fromDate == null  && toDate == null && (keyword == null || keyword.isEmpty());
    }
}
