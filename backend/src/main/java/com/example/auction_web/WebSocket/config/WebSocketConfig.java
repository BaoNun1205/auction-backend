package com.example.auction_web.WebSocket.config;

import com.example.auction_web.WebSocket.util.WebSocketAuthenticationInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private WebSocketAuthenticationInterceptor webSocketAuthenticationInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app"); // client send server
        registry.enableSimpleBroker("/rt-product"); // server send client
    }

    // endpoint websocket. user start connect to this endpoint
    // withSockJS() for websocket not supported browser
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rt-auction")
                .addInterceptors(webSocketAuthenticationInterceptor)
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

}
