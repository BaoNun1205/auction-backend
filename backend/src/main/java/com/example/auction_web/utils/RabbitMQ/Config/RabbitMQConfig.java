package com.example.auction_web.utils.RabbitMQ.Config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue bidQueue() {
        return new Queue("bidQueue");
    }

    @Bean
    public Queue sessionEndQueue() {
        return new Queue("sessionEndQueue");
    }

    @Bean
    public TopicExchange bidExchange() {
        return new TopicExchange("bidExchange");
    }

    @Bean TopicExchange sessionEndExchange() {
        return new TopicExchange("sessionEndExchange");
    }

    @Bean
    public Binding bidBinding(Queue bidQueue, TopicExchange bidExchange) {
        return BindingBuilder.bind(bidQueue).to(bidExchange).with("bid");
    }

    @Bean
    public Binding sessionEndBinding(Queue sessionEndQueue, TopicExchange sessionEndExchange) {
        return BindingBuilder.bind(sessionEndQueue).to(sessionEndExchange).with("session");
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
