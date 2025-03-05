package com.advanceit.bankservice.async.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfigBinding {

    public static final String CLIENT_EXCHANGE = "client-exchange";
    public static final String ACCOUNT_EXCHANGE = "account-exchange";

    public static final String CLIENT_QUEUE = "client-queue";
    public static final String ACCOUNT_QUEUE = "account-queue";

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue clientQueue() {
        return new Queue(CLIENT_QUEUE, true);
    }

    @Bean
    public Queue accountQueue() {
        return new Queue(ACCOUNT_QUEUE, true);
    }

    @Bean
    public TopicExchange clientExchange() {
        return new TopicExchange(CLIENT_EXCHANGE);
    }

    @Bean
    public TopicExchange accountExchange() {
        return new TopicExchange(ACCOUNT_EXCHANGE);
    }

    @Bean
    public Binding clientBinding(Queue clientQueue, TopicExchange clientExchange) {
        return BindingBuilder
                .bind(clientQueue)
                .to(clientExchange)
                .with("client.#");
    }

    @Bean
    public Binding accountBinding(Queue accountQueue, TopicExchange accountExchange) {
        return BindingBuilder
                .bind(accountQueue)
                .to(accountExchange)
                .with("account.#");
    }
}
