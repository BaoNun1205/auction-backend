package com.example.auction_web.service;

import com.example.auction_web.entity.EmailVerificationToken;
import com.example.auction_web.entity.auth.User;

import java.util.concurrent.CompletableFuture;

public interface EmailVerificationTokenService {
    CompletableFuture<Void> sendEmailConfirmation(String recipientEmail);

    Boolean confirmAccountRegistration(String token);

    boolean isTokenExpired(EmailVerificationToken token);

    User checkPasswordResetEmail(String email);

    CompletableFuture<Void> sendPasswordResetEmail(User user);

    boolean resetPassword(String token, String password);
}
