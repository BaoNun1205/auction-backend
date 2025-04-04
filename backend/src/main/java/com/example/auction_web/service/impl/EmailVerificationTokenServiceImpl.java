package com.example.auction_web.service.impl;

import com.example.auction_web.dto.request.BalanceUserCreateRequest;
import com.example.auction_web.entity.EmailVerificationToken;
import com.example.auction_web.entity.auth.User;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.example.auction_web.mapper.BalanceUserMapper;
import com.example.auction_web.repository.BalanceUserRepository;
import com.example.auction_web.repository.EmailVerificationTokenRepository;
import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.service.EmailVerificationTokenService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Console;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class EmailVerificationTokenServiceImpl implements EmailVerificationTokenService {
    EmailVerificationTokenRepository emailVerificationTokenRepository;
    BalanceUserRepository balanceUserRepository;
    BalanceUserMapper balanceUserMapper;
    JavaMailSender javaMailSender;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Async
    @Override
    public CompletableFuture<Void> sendEmailConfirmation(String recipientEmail) {
        String token = java.util.UUID.randomUUID().toString();
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(recipientEmail, token);
        emailVerificationTokenRepository.save(emailVerificationToken);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("webonlineauction@gmail.com", "Online Auction Support");
            helper.setTo(recipientEmail);

            String subject = "Please confirm your account";
            String link = "https://auction-frontend-ebon.vercel.app/confirm-account?token=" + token;

            String content = "<p>Hello,</p>" +
                    "<p>Thank you for registering with Online Auction.</p>"
                    + "<p>Please click the link below to confirm your account:</p>" +
                    "<p><a href=\"" + link + "\">Confirm my account</a></p>" + "<br>"
                    + "<p>If you did not register for this account, please ignore this email.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.EMAIL_VERIFICATION_TOKEN_NOT_EXIST);
        }

        javaMailSender.send(message);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional
    public Boolean confirmAccountRegistration(String token) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token);

        if (emailVerificationToken == null) {
            throw new AppException(ErrorCode.EMAIL_VERIFICATION_TOKEN_NOT_EXIST);
        }

        User user = userRepository.findUserByEmail(emailVerificationToken.getUserEmail());
        if (user != null) {
            if (!isTokenExpired(emailVerificationToken)) {
                user.setEnabled(true);
                userRepository.save(user);

                BalanceUserCreateRequest balanceUserCreateRequest = new BalanceUserCreateRequest().builder()
                        .userId(user.getUserId())
                        .accountBalance(BigDecimal.valueOf(0))
                        .build();
                var balanUser = balanceUserMapper.toBalanceUser(balanceUserCreateRequest);
                balanUser.setUser(user);
                balanceUserRepository.save(balanUser);
                //delete token from database
                emailVerificationTokenRepository.deleteEmailVerificationTokenByUserEmail(user.getEmail());
                return true;
            } else {
                emailVerificationTokenRepository.deleteEmailVerificationTokenByUserEmail(emailVerificationToken.getUserEmail());

                sendEmailConfirmation(user.getEmail());
                return false;
            }
        } else {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }
    }

    public boolean isTokenExpired(EmailVerificationToken token) {
        return LocalDateTime.now().isAfter(token.getExpiryDate());
    }

    @Override
    public User checkPasswordResetEmail(String email) {
        return null;
    }

    @Async
    @Override
    public CompletableFuture<Void> sendPasswordResetEmail(User user) {
        String token = UUID.randomUUID().toString();

        EmailVerificationToken emailVerificationToken = new EmailVerificationToken(user.getEmail(), token);
        emailVerificationTokenRepository.save(emailVerificationToken);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("webonlineauction.com", "Online Auction Support");
            helper.setTo(user.getEmail());

            String subject = "Reset your password";
            String link = "http://localhost:3000/reset-password?token=" + token;

            String content = "<p>Hello,</p>" +
                    "<p>We received a request to reset your password.</p>"
                    + "<p>Please click the link below to reset your password:</p>" +
                    "<p><a href=\"" + link + "\">Reset my password</a></p>" + "<br>"
                    + "<p>If you did not request a password reset, please ignore this email.</p>";

            helper.setSubject(subject);
            helper.setText(content, true);
        } catch (UnsupportedEncodingException | MessagingException e) {
            e.printStackTrace();
            throw new AppException(ErrorCode.EMAIL_VERIFICATION_TOKEN_NOT_EXIST);
        }

        javaMailSender.send(message);
        return CompletableFuture.completedFuture(null);
    }

    @Override
    @Transactional
    public boolean resetPassword(String token, String password) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token);

        if (emailVerificationToken == null) {
            throw new AppException(ErrorCode.EMAIL_VERIFICATION_TOKEN_NOT_EXIST);
        }

        User user = userRepository.findUserByEmail(emailVerificationToken.getUserEmail());
        if (user == null) {
            throw new AppException(ErrorCode.USER_NOT_EXISTED);
        }

        if (!isTokenExpired(emailVerificationToken)) {
            // set new password
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            //delete token from database
            emailVerificationTokenRepository.deleteEmailVerificationTokenByUserEmail(emailVerificationToken.getUserEmail());
            return true;
        } else {
            //delete old token
            emailVerificationTokenRepository.deleteEmailVerificationTokenByUserEmail(emailVerificationToken.getUserEmail());

            //send new token
            sendPasswordResetEmail(user);
            return false;
        }
    }
}
