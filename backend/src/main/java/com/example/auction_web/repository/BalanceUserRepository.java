package com.example.auction_web.repository;

import com.example.auction_web.entity.BalanceUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface BalanceUserRepository extends JpaRepository<BalanceUser, String> {
    BalanceUser findBalanceUserByUser_UserId(String userId);
    BalanceUser findBalanceUserByUser_Email(String email);

    @Modifying
    @Transactional
    @Query("update BalanceUser b set b.accountBalance = b.accountBalance + :amount where b.user.userId = :userId")
    int increaseBalance(String userId, BigDecimal amount);

    @Modifying
    @Transactional
    @Query("update BalanceUser b set b.accountBalance = b.accountBalance - :amount where b.user.userId = :userId")
    int minusBalance(String userId, BigDecimal amount);
}
