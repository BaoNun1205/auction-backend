package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionHistoryCreateRequest;
import com.example.auction_web.dto.request.AuctionHistoryUpdateRequest;
import com.example.auction_web.dto.response.AuctionHistoryResponse;
import com.example.auction_web.entity.AuctionHistory;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionHistoryMapper;
import com.example.auction_web.repository.AuctionHistoryRepository;
import com.example.auction_web.repository.AuctionItemRepository;
import com.example.auction_web.service.AuctionHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class AuctionHistoryServiceImpl implements AuctionHistoryService {
    // init
    AuctionHistoryRepository auctionHistoryRepository;
    AuctionItemRepository auctionItemRepository;
    AuctionHistoryMapper auctionHistoryMapper;

    //create AuctionHistory
    public AuctionHistoryResponse createAuctionHistory(AuctionHistoryCreateRequest request) {
        var auctionHistory = auctionHistoryMapper.toAuctionHistory(request);
        setAuctionHistoryReference(request, auctionHistory);
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));
    }

    //Update AuctionHistory
    public AuctionHistoryResponse updateAuctionHistory(String id, AuctionHistoryUpdateRequest request) {
        AuctionHistory auctionHistory = auctionHistoryRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_HISTORY_NOT_EXISTED));
        auctionHistoryMapper.updateAuctionHistoryFromRequest(auctionHistory, request);
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.save(auctionHistory));
    }

    // get all AuctionHistories
    public List<AuctionHistoryResponse> getAllAuctionHistories() {
        return auctionHistoryRepository.findAll().stream()
                .map(auctionHistoryMapper::toAuctionHistoryResponse)
                .toList();
    }

    //get AuctionHistories by AuctionItemId
    public AuctionHistoryResponse getAuctionHistoriesByAuctionItemId(String auctionItemId) {
        if (!auctionItemRepository.existsById(auctionItemId)) {
            throw new AppException(ErrorCode.AUCTION_ITEM_NOT_EXISTED);
        }
        return auctionHistoryMapper.toAuctionHistoryResponse(auctionHistoryRepository.findAuctionHistoryByAuctionItem_AuctionItemId(auctionItemId));
    }

    //set AuctionItem for AuctionHistory
    void setAuctionHistoryReference(AuctionHistoryCreateRequest request, AuctionHistory auctionHistory) {
        auctionHistory.setAuctionItem(getAuctionItemById(request.getAuctionItemId()));
    }

    //get AuctionItem by AuctionItemId
    AuctionItem getAuctionItemById(String auctionItemId) {
        return auctionItemRepository.findById(auctionItemId)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_ITEM_NOT_EXISTED));
    }
}
