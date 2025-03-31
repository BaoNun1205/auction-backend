package com.example.auction_web.utils.RabbitMQ.Producer;

import com.example.auction_web.utils.RabbitMQ.Dto.SessionEndMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionEndProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendSessionEndEvent(SessionEndMessage sessionEndMessage) {
        rabbitTemplate.convertAndSend("sessionEndExchange", "session", sessionEndMessage);
    }
}
