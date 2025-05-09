package com.example.auction_web.personalization.service.impl;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.personalization.SearchHistory;
import com.example.auction_web.mapper.SearchHistoryMapper;
import com.example.auction_web.personalization.dto.response.SearchHistoryResponse;
import com.example.auction_web.personalization.repository.SearchHistoryRepository;
import com.example.auction_web.personalization.service.SearchHistoryService;
import com.example.auction_web.service.auth.UserService;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class SearchHistoryServiceImpl implements SearchHistoryService {

    SearchHistoryRepository searchHistoryRepository;
    UserService userService;
    SearchHistoryMapper searchHistoryMapper;

    @Override
    public void recordSearch(String userId, String keyword) {
        User user = userService.getUser(userId);
        Optional<SearchHistory> existing = searchHistoryRepository
                .findByUserAndKeywordAndDelFlagFalse(user, keyword);

        if (existing.isPresent()) {
            SearchHistory history = existing.get();
            history.setSearchCount(history.getSearchCount() + 1);
            history.setUpdatedAt(LocalDateTime.now());
            searchHistoryRepository.save(history);
        } else {
            SearchHistory history = SearchHistory.builder()
                    .user(user)
                    .keyword(keyword)
                    .build();
            searchHistoryRepository.save(history);
        }
    }

    @Override
    public List<SearchHistoryResponse> getTopKeywords(String userId) {
        User user = userService.getUser(userId);
        return searchHistoryRepository.findByUserAndDelFlagFalseOrderBySearchCountDesc(user)
                .stream()
                .map(searchHistoryMapper::toSearchHistoryResponse)
                .toList();
    }

    @Override
    public List<SearchHistoryResponse> getRecentKeywords(String userId) {
        User user = userService.getUser(userId);
        return searchHistoryRepository.findByUserAndDelFlagFalseOrderByUpdatedAtDesc(user)
                .stream()
                .map(searchHistoryMapper::toSearchHistoryResponse)
                .toList();
    }
}

