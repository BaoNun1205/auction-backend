package com.example.auction_web.WebSocket.config;

import com.example.auction_web.WebSocket.util.CustomerWebSocketHandler;
import com.example.auction_web.WebSocket.util.WebSocketAuthenticationInterceptor;
import com.example.auction_web.configuration.CustomJwtDecoder;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.out;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private final CustomJwtDecoder customJwtDecoder;
    @Autowired
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/rt-product");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rt-auction")
                .setAllowedOrigins("http://127.0.0.1:5500")
                .withSockJS();
    }


    @Override
    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);

        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        converter.setObjectMapper(objectMapper);

        converter.setContentTypeResolver(resolver);
        messageConverters.add(converter);
        return false;
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration
                .interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

                out.println("accessor: " + accessor);

                if (accessor.getCommand() != StompCommand.CONNECT) {
                    // Handle other stomp commands if needed
                    return message;
                }

                List<String> authHeaders = accessor.getNativeHeader("Authorization");
                out.println("authHeaders: " + authHeaders);
                if (authHeaders == null || authHeaders.isEmpty()) {
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                }

                String bearerToken = authHeaders.get(0);
                String token = resolveToken(bearerToken);
                out.println("token: " + token);
                if (token == null) {
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                }

                out.println("Tiep theo");
                try {
                    out.println("Xác thuc JWT");
                    Jwt jwt = customJwtDecoder.decode(token);
                    if (jwt != null) {
                        out.println("jwt: " + jwt);
                    } else {
                        out.println("jwt is null");
                    }
                    Authentication authentication = jwtAuthenticationConverter.convert(jwt);
                    out.println("authentication: " + authentication);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (JwtException e) {
                    out.println("Exception: " + e);
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
                out.println("message: " + message);
                return message;
            }
        });
    }



    private String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
