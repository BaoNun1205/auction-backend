package com.example.auction_web.controller;

import com.example.auction_web.dto.request.AuctionItemCreateRequest;
import com.example.auction_web.dto.request.AuctionItemUpdateRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.dto.response.AuctionItemResponse;
import com.example.auction_web.service.AuctionItemService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auction-items")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AuctionItemController {
    AuctionItemService auctionItemService;

    @PostMapping
    ApiResponse<AuctionItemResponse> createAuctionItem(@RequestBody AuctionItemCreateRequest request) {
        return ApiResponse.<AuctionItemResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionItemService.createAuctionItem(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<AuctionItemResponse> updateAuctionItem(@PathVariable String id, @RequestBody AuctionItemUpdateRequest request) {
        return ApiResponse.<AuctionItemResponse>builder()
                .code(HttpStatus.OK.value())
                .result(auctionItemService.updateAuctionItem(id, request))
                .build();
    }

    @GetMapping
    ApiResponse<List<AuctionItemResponse>> getAllAuctionItems() {
        return ApiResponse.<List<AuctionItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionItemService.getAllAuctionItems())
                .build();
    }

    @GetMapping("/auction-session/id/{auctionSessionId}")
    ApiResponse<List<AuctionItemResponse>> getAuctionItemsByAuctionSessionId(@PathVariable String auctionSessionId) {
        return ApiResponse.<List<AuctionItemResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(auctionItemService.getAuctionItemsByAuctionSessionId(auctionSessionId))
                .build();
    }
}
