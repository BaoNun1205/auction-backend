package com.example.auction_web.utils.RabbitMQ.Producer;

import com.example.auction_web.utils.RabbitMQ.Dto.BidMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BidEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void sendBidEvent(BidMessage bidMessage) {
        rabbitTemplate.convertAndSend("bidExchange", "bid", bidMessage);
    }
}
