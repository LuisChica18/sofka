package com.advanceit.clientservice.async.listener;

import com.advanceit.clientservice.async.dto.AccountDTO;
import com.advanceit.clientservice.async.dto.MessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.advanceit.clientservice.async.config.RabbitMQConfigBinding.CLIENT_QUEUE;

@Component
@RequiredArgsConstructor
public class AccountMessageListener {
    @RabbitListener(queues = CLIENT_QUEUE)
    public void receiveMessage(MessageDTO<AccountDTO> message) {

        switch (message.getOperationType()) {
            case CREATE:
                System.out.println("Received Account Creation: " + message.getPayload());
                break;
            case UPDATE:
                System.out.println("Received Account Update: " + message.getPayload());
                break;
            case DELETE:
                System.out.println("Received Account Deletion: " + message.getPayload());
                break;
        }
    }
}