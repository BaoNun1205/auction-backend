package com.example.auction_web.repository;

import com.example.auction_web.entity.BalanceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistory, String> {
    List<BalanceHistory> findBalanceHistoriesByBalanceUser_BalanceUserId(String balanceUserId);
    List<BalanceHistory> findBalanceHistoriesByBalanceUser_User_UserId(String userId);
}
