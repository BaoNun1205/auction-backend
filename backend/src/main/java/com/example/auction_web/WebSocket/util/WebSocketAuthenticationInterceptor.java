package com.example.auction_web.WebSocket.util;

import com.example.auction_web.configuration.CustomJwtDecoder;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class WebSocketAuthenticationInterceptor implements ChannelInterceptor {

    private final CustomJwtDecoder jwtUtil;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (accessor.getCommand() != StompCommand.CONNECT) {
            // Handle other stomp commands if needed
            return message;
        }

        List<String> authHeaders = accessor.getNativeHeader("Authorization");
        if (authHeaders == null || authHeaders.isEmpty()) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        String bearerToken = authHeaders.get(0);
        String token = resolveToken(bearerToken);
        if (token == null) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        try {
            Jwt jwt = jwtUtil.decode(token);
            Authentication authentication = jwtAuthenticationConverter.convert(jwt);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (JwtException e) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return message;
    }

    private String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
