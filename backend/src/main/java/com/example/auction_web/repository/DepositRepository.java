package com.example.auction_web.repository;

import com.example.auction_web.entity.CoinUser;
import com.example.auction_web.entity.Deposit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepositRepository extends JpaRepository<Deposit, String> {
}
