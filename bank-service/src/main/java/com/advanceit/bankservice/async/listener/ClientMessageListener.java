package com.advanceit.bankservice.async.listener;

import com.advanceit.bankservice.async.dto.ClientDTO;
import com.advanceit.bankservice.async.dto.MessageDTO;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.advanceit.bankservice.async.config.RabbitMQConfigBinding.ACCOUNT_QUEUE;

@Component
@RequiredArgsConstructor
@Data
@Slf4j
public class ClientMessageListener {

    private List<ClientDTO> clientList;

    @RabbitListener(queues = ACCOUNT_QUEUE)
    public void receiveMessage(MessageDTO<ClientDTO> message) {

        switch (message.getOperationType()) {
            case CREATE:
                clientList.add(message.getPayload());
                break;
            case UPDATE, DELETE:
                log.info("Implement not yet");
                break;
        }
    }
}
