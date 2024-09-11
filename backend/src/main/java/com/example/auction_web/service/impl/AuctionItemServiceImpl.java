package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionItemCreateRequest;
import com.example.auction_web.dto.request.AuctionItemUpdateRequest;
import com.example.auction_web.dto.response.AuctionItemResponse;
import com.example.auction_web.entity.Asset;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.entity.AuctionSession;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.AuctionItemMapper;
import com.example.auction_web.repository.AssetRepository;
import com.example.auction_web.repository.AuctionItemRepository;
import com.example.auction_web.repository.AuctionSessionRepository;
import com.example.auction_web.service.AuctionItemService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionItemServiceImpl implements AuctionItemService {
    AuctionItemRepository auctionItemRepository;
    AuctionSessionRepository auctionSessionRepository;
    AssetRepository assetRepository;
    AuctionItemMapper auctionItemMapper;

    public AuctionItemResponse createAuctionItem(AuctionItemCreateRequest request) {
        var auctionSession = auctionItemMapper.toAuctionItem(request);
        setAuctionSessionReference(request, auctionSession);
        return auctionItemMapper.toAuctionItemResponse(auctionItemRepository.save(auctionSession));
    }

    public AuctionItemResponse updateAuctionItem(String id, AuctionItemUpdateRequest request) {
        AuctionItem auctionItem = auctionItemRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.AUCTION_ITEM_NOT_EXISTED));
        auctionItemMapper.updateAuctionItem(auctionItem, request);
        return auctionItemMapper.toAuctionItemResponse(auctionItemRepository.save(auctionItem));
    }

    public List<AuctionItemResponse> getAllAuctionItems() {
        return auctionItemRepository.findAll().stream()
                .map(auctionItemMapper::toAuctionItemResponse)
                .toList();
    }

    public List<AuctionItemResponse> getAuctionItemsByAuctionSessionId(String auctionSessionId) {
        if (!auctionSessionRepository.existsById(auctionSessionId)) {
            throw new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED);
        }
        return auctionItemRepository.findAuctionItemByAuctionSession_AuctionSessionId(auctionSessionId).stream()
                .map(auctionItemMapper::toAuctionItemResponse)
                .toList();
    }

    private void setAuctionSessionReference(AuctionItemCreateRequest request, AuctionItem auctionItem) {
        auctionItem.setAuctionSession(getAuctionSessionById(request.getAuctionSessionId()));
        auctionItem.setAsset(getAssetById(request.getAssetId()));
    }


    AuctionSession getAuctionSessionById(String id) {
        return auctionSessionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.AUCTION_SESSION_NOT_EXISTED));
    }

    Asset getAssetById(String id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ASSET_NOT_EXISTED));
    }
}
