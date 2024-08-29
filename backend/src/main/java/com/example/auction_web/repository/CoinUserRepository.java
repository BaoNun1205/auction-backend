package com.example.auction_web.repository;

import com.example.auction_web.entity.Category;
import com.example.auction_web.entity.CoinUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinUserRepository extends JpaRepository<CoinUser, String> {
}
