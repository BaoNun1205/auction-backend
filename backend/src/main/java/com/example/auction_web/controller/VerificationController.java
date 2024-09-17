package com.example.auction_web.controller;

import com.example.auction_web.dto.request.EmailTokenRequest;
import com.example.auction_web.dto.request.ResetPasswordRequest;
import com.example.auction_web.dto.response.ApiResponse;
import com.example.auction_web.service.EmailVerificationTokenService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class VerificationController {
    EmailVerificationTokenService emailVerificationTokenService;

    @PostMapping("/account-registration")
    ApiResponse<Object> confirmAccountRegistration(@RequestBody EmailTokenRequest token) {
        boolean isVerification =  emailVerificationTokenService.confirmAccountRegistration(token.getToken());
        String message = isVerification ? "Account is confirmed successfully" : "Reset password link is expired. A new confirmation email has been sent.";
        int statusCode = isVerification ? HttpStatus.OK.value() : HttpStatus.ACCEPTED.value();
        return ApiResponse.builder()
                .code(statusCode)
                .message(message)
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Object> sendResetPassword(@RequestBody ResetPasswordRequest request) {
        boolean isSuccess = emailVerificationTokenService.resetPassword(request.getToken(), request.getPassword());
        String message = isSuccess ? "Password is changed successfully" : "Token expired. A new reset password email has been sent.";
        int statusCode = isSuccess ? HttpStatus.OK.value() : HttpStatus.ACCEPTED.value();

        return ApiResponse.builder()
                .code(statusCode)
                .message(message)
                .build();
    }
}
