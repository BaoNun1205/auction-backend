package com.example.auction_web.repository;

import com.example.auction_web.entity.BalanceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceUserRepository extends JpaRepository<BalanceUser, String> {
    BalanceUser findBalanceUserByUser_UserId(String userId);
}
