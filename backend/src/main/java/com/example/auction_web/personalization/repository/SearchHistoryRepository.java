package com.example.auction_web.personalization.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auction_web.entity.auth.User;
import com.example.auction_web.entity.personalization.SearchHistory;

@Repository
public interface SearchHistoryRepository extends JpaRepository<SearchHistory, String> {
    Optional<SearchHistory> findByUserAndKeywordAndDelFlagFalse(User user, String keyword);
    List<SearchHistory> findByUserAndDelFlagFalseOrderBySearchCountDesc(User user);
    List<SearchHistory> findByUserAndDelFlagFalseOrderByUpdatedAtDesc(User user);
    List<SearchHistory> findByKeywordAndDelFlagFalse(String keyword);
}

