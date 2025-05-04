package com.example.auction_web.mapper;

import org.mapstruct.Mapper;

import com.example.auction_web.dto.response.personalization.SearchHistoryResponse;
import com.example.auction_web.entity.personalization.SearchHistory;

@Mapper(componentModel = "spring")
public interface SearchHistoryMapper {
    SearchHistoryResponse toSearchHistoryResponse(SearchHistory searchHistory);
}

