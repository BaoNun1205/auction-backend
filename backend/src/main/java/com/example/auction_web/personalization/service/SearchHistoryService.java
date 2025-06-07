package com.example.auction_web.personalization.service;

import java.util.List;

import com.example.auction_web.personalization.dto.response.SearchHistoryResponse;

public interface SearchHistoryService {
    void recordSearch(String userId, String keyword);
    List<SearchHistoryResponse> getTopKeywords(String userId);
    List<SearchHistoryResponse> getRecentKeywords(String userId);
}

