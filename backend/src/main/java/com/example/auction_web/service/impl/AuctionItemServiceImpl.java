package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.AuctionItemCreateRequest;
import com.example.auction_web.dto.request.AuctionItemUpdateRequest;
import com.example.auction_web.dto.response.AuctionItemResponse;
import com.example.auction_web.entity.AuctionItem;
import com.example.auction_web.mapper.AuctionItemMapper;
import com.example.auction_web.repository.AuctionItemRepository;
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
    AuctionItemMapper auctionItemMapper;

    public AuctionItemResponse createAuctionItem(AuctionItemCreateRequest request) {
        return auctionItemMapper.toAuctionItemResponse(auctionItemRepository.save(auctionItemMapper.toAuctionItem(request)));
    }

    public AuctionItemResponse updateAuctionItem(String id, AuctionItemUpdateRequest request) {
        AuctionItem auctionItem = auctionItemRepository.findById(id).orElseThrow();
        auctionItemMapper.updateAuctionItem(auctionItem, request);
        return auctionItemMapper.toAuctionItemResponse(auctionItemRepository.save(auctionItem));
    }

    public List<AuctionItemResponse> getAllAuctionItems() {
        return auctionItemRepository.findAll().stream()
                .map(auctionItemMapper::toAuctionItemResponse)
                .toList();
    }

    public List<AuctionItemResponse> getAuctionItemsByAuctionSessionId(String auctionSessionId) {
        return auctionItemRepository.findByAuctionSessionId(auctionSessionId).stream()
                .map(auctionItemMapper::toAuctionItemResponse)
                .toList();
    }
}
