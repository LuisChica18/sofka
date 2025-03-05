package com.advanceit.clientservice.async.service;

import com.advanceit.clientservice.async.dto.MessageDTO;
import com.advanceit.clientservice.async.dto.OperationType;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagePublisherService {
    private final RabbitTemplate rabbitTemplate;

    public <T> void publishMessage(
            String exchange,
            String routingKey,
            T payload,
            OperationType operationType,
            String sourceService
    ) {
        MessageDTO<T> message = MessageDTO.<T>builder()
                .operationType(operationType)
                .payload(payload)
                .sourceService(sourceService)
                .build();

        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}