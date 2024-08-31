package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionHistoryDetailCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryDetailDeleteRequest;
import com.example.auction_web.dto.response.AuctionHistoryDetailResponse;
import com.example.auction_web.entity.AuctionHistoryDetail;
import com.example.auction_web.mapper.AuctionHistoryDetailMapper;
import com.example.auction_web.repository.AuctionHistoryDetailRepository;
import com.example.auction_web.service.AuctionHistoryDetailService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuctionHistoryDetailServiceImpl implements AuctionHistoryDetailService {
    AuctionHistoryDetailRepository auctionHistoryDetailRepository;
    AuctionHistoryDetailMapper auctionHistoryDetailMapper;

    public AuctionHistoryDetailResponse createAuctionHistoryDetail(AuctionHistoryDetailCreateRequest request) {
        return auctionHistoryDetailMapper.toAuctionHistoryDetailResponse(auctionHistoryDetailRepository.save(auctionHistoryDetailMapper.toAuctionHistoryDetail(request)));
    }

    public AuctionHistoryDetailResponse deleteAuctionHistoryDetail(String id, AuctionHistoryDetailDeleteRequest request) {
        AuctionHistoryDetail auctionHistoryDetail = auctionHistoryDetailRepository.findById(id).orElseThrow();
        auctionHistoryDetailMapper.deleteAuctionHistoryDetail(auctionHistoryDetail, request);
        return auctionHistoryDetailMapper.toAuctionHistoryDetailResponse(auctionHistoryDetailRepository.save(auctionHistoryDetail));
    }

    public List<AuctionHistoryDetailResponse> getAllAuctionHistoryDetails() {
        return auctionHistoryDetailRepository.findAll().stream()
                .map(auctionHistoryDetailMapper::toAuctionHistoryDetailResponse)
                .toList();
    }

    public List<AuctionHistoryDetailResponse> getAllByAuctionHistoryId(String auctionHistoryId) {
       return auctionHistoryDetailRepository.findAllByAuctionHistoryId(auctionHistoryId).stream()
               .map(auctionHistoryDetailMapper::toAuctionHistoryDetailResponse)
               .toList();
    }
}
