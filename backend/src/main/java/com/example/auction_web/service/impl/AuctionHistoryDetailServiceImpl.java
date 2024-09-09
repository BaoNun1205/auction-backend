package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionHistoryDetailCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryDetailDeleteRequest;
import com.example.auction_web.dto.response.AuctionHistoryDetailResponse;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionHistoryDetail;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionHistoryDetailMapper;
import com.example.auction_web.repository.AuctionHistoryDetailRepository;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.AuctionHistoryDetailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class AuctionHistoryDetailServiceImpl implements AuctionHistoryDetailService {
    AuctionHistoryDetailRepository auctionHistoryDetailRepository;
    AuctionHistoryRepository auctionHistoryRepository;
    UserRepository userRepository;
    AuctionHistoryDetailMapper auctionHistoryDetailMapper;

    //create AuctionHistoryDetail
    public AuctionHistoryDetailResponse createAuctionHistoryDetail(AuctionHistoryDetailCreateRequest request) {
        var auctionHistoryDetail = auctionHistoryDetailMapper.toAuctionHistoryDetail(request);
        setAuctionHistoryDetailReference(request, auctionHistoryDetail);
        return auctionHistoryDetailMapper.toAuctionHistoryDetailResponse(auctionHistoryDetailRepository.save(auctionHistoryDetail));
    }

    //Update AuctionHistoryDetail
    public AuctionHistoryDetailResponse deleteAuctionHistoryDetail(String id, AuctionHistoryDetailDeleteRequest request) {
        AuctionHistoryDetail auctionHistoryDetail = auctionHistoryDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_HISTORY_DETAIL_NOT_EXISTED));
        auctionHistoryDetailMapper.deleteAuctionHistoryDetail(auctionHistoryDetail, request);
        return auctionHistoryDetailMapper.toAuctionHistoryDetailResponse(auctionHistoryDetailRepository.save(auctionHistoryDetail));
    }

    // get all AuctionHistoryDetails
    public List<AuctionHistoryDetailResponse> getAllAuctionHistoryDetails() {
        return auctionHistoryDetailRepository.findAll().stream()
                .map(auctionHistoryDetailMapper::toAuctionHistoryDetailResponse)
                .toList();
    }
    // get all AuctionHistoryDetails by AuctionHistoryId
    public List<AuctionHistoryDetailResponse> getAllByAuctionHistoryID(String auctionHistoryId) {
       return auctionHistoryDetailRepository.findAuctionHistoryDetailsByAuctionHistory_AuctionHistoryId(auctionHistoryId).stream()
               .map(auctionHistoryDetailMapper::toAuctionHistoryDetailResponse)
               .toList();
    }

    // get all AuctionHistoryDetails by UserId
    public List<AuctionHistoryDetailResponse> getAllByUserId(String userId) {
        return auctionHistoryDetailRepository.findAuctionHistoryDetailsByUser_UserId(userId).stream()
                .map(auctionHistoryDetailMapper::toAuctionHistoryDetailResponse)
                .toList();
    }

    //set AuctionHistory and User for AuctionHistoryDetail
    void setAuctionHistoryDetailReference(AuctionHistoryDetailCreateRequest request, AuctionHistoryDetail auctionHistoryDetail) {
        auctionHistoryDetail.setAuctionHistory(getAuctionHistoryById(request.getAuctionHistoryId()));
        auctionHistoryDetail.setUser(getUserById(request.getUserId()));
    }

    // get AuctionHistory by AuctionHistoryId
    AuctionHistory getAuctionHistoryById(String auctionHistoryId) {
        return auctionHistoryRepository.findById(auctionHistoryId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_HISTORY_NOT_EXISTED));
    }

    // get User by UserId
    User getUserById(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
