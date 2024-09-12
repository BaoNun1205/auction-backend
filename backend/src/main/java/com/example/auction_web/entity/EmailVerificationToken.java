package com.example.auction_web.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class EmailVerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String emailVerificationTokenId;

    String userEmail;
    String token;
    LocalDateTime expiryDate;

    public EmailVerificationToken(String userEmail, String token) {
        this.userEmail = userEmail;
        this.token = token;
        this.expiryDate = calculatedExpiryDate(3);
    }

    LocalDateTime calculatedExpiryDate(int days) {
        return LocalDateTime.now().plusDays(days);
    }
}
