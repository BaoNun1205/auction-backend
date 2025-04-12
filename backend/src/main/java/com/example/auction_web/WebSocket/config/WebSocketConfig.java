package com.example.auction_web.WebSocket.config;

import com.example.auction_web.configuration.CustomJwtDecoder;
import com.example.auction_web.exception.AppException;
import com.example.auction_web.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;


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
        registry.enableSimpleBroker("/rt-product", "/rt-chat", "/rt-notification");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/rt-auction")
                .setAllowedOrigins("http://127.0.0.1:5500", "https://auction-frontend-ebon.vercel.app", "http://localhost:5173", "http://127.0.0.1:5173");
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
                    Jwt jwt = customJwtDecoder.decode(token);
                    Authentication authentication = jwtAuthenticationConverter.convert(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } catch (JwtException e) {
                    throw new AppException(ErrorCode.UNAUTHENTICATED);
                }
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
