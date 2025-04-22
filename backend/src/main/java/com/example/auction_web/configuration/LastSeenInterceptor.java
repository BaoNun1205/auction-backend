package com.example.auction_web.configuration;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.example.auction_web.repository.auth.UserRepository;
import com.example.auction_web.utils.JwtTokenUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Slf4j
public class LastSeenInterceptor implements HandlerInterceptor {

    UserRepository userRepository;
    JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String token = jwtTokenUtil.resolveToken(request);
            if (token != null && jwtTokenUtil.validateToken(token)) {
                String userId = jwtTokenUtil.getUserIdFromToken(token);
                userRepository.findById(userId).ifPresent(user -> {
                    user.setLastSeen(LocalDateTime.now());
                    userRepository.save(user);
                });
            }
        } catch (Exception e) {
            log.warn("Failed to update last seen: {}", e.getMessage());
        }
        return true;
    }
}
