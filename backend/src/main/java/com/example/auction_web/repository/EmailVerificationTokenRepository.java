package com.example.auction_web.repository;

import com.example.auction_web.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, String> {
    EmailVerificationToken findByToken(String token);

    void deleteEmailVerificationTokenByUserEmail(String userEmail);
}
