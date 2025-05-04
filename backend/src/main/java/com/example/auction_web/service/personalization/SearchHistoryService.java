package com.example.auction_web.service.personalization;

import java.util.List;

import com.example.auction_web.dto.response.personalization.SearchHistoryResponse;

public interface SearchHistoryService {
    void recordSearch(String userId, String keyword);
    List<SearchHistoryResponse> getTopKeywords(String userId);
    List<SearchHistoryResponse> getRecentKeywords(String userId);
}
