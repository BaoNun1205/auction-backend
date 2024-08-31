package com.example.auction_web.service.auth;

import com.example.auction_web.dto.request.auth.AuthenticationRequest;
import com.example.auction_web.dto.request.auth.IntrospectRequest;
import com.example.auction_web.dto.request.auth.LogoutRequest;
import com.example.auction_web.dto.request.auth.RefreshRequest;
import com.example.auction_web.dto.response.auth.AuthenticationResponse;
import com.example.auction_web.dto.response.auth.IntrospectResponse;
import com.example.auction_web.entity.auth.User;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException;
    void logout(LogoutRequest request) throws ParseException, JOSEException;
    public AuthenticationResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException;
    String generateToken(User user);
    SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException;
    String buildScope(User user);
}
