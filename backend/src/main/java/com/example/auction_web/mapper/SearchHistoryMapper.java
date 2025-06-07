package com.example.auction_web.mapper;

import org.mapstruct.Mapper;

import com.example.auction_web.entity.personalization.SearchHistory;
import com.example.auction_web.personalization.dto.response.SearchHistoryResponse;

@Mapper(componentModel = "spring")
public interface SearchHistoryMapper {
    SearchHistoryResponse toSearchHistoryResponse(SearchHistory searchHistory);
}

