package com.example.auction_web.utils;

import java.text.ParseException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil {

    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            // Chỉ cần parse được là ok, không verify signature ở đây (hoặc bạn có thể thêm vào)
            return signedJWT.getJWTClaimsSet().getExpirationTime().after(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        return signedJWT.getJWTClaimsSet().getJWTID(); // userId dưới dạng String
    }
}
