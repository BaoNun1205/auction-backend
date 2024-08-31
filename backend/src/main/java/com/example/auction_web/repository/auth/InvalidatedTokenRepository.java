package com.example.auction_web.repository.auth;

import com.example.auction_web.entity.auth.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String>  {
}
