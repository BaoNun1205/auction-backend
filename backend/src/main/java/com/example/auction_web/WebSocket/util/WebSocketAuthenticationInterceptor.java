package com.example.auction_web.WebSocket.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.List;
import java.util.Map;

@Component
public class WebSocketAuthenticationInterceptor implements HandshakeInterceptor {
    @Autowired
    private JwtDecoder jwtDecoder;
    

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        
        String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Jwt jwt = jwtDecoder.decode(token);
                Authentication authentication = new UsernamePasswordAuthenticationToken(
                        jwt.getSubject(),
                        null,
                        jwtGrantedAuthoritiesConverter.convert(jwt) // Sử dụng converter để lấy quyền
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                attributes.put("user", authentication);
                return true;
            } catch (Exception e) {
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }
        }

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }
}
