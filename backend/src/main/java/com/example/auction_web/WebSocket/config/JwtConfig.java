package com.example.auction_web.WebSocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

@Configuration
public class JwtConfig {

    @Bean
    public JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter() {
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        converter.setAuthorityPrefix("ROLE_"); // Hoặc bất kỳ prefix nào bạn muốn sử dụng
        converter.setAuthoritiesClaimName("authorities"); // Tên claim chứa quyền trong JWT
        return converter;
    }
}
