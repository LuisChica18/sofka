package com.advanceit.clientservice.service.impl;

import com.advanceit.clientservice.async.dto.OperationType;
import com.advanceit.clientservice.async.service.MessagePublisherService;
import com.advanceit.clientservice.dto.ClientDTO;
import com.advanceit.clientservice.entity.Client;
import com.advanceit.clientservice.exception.ResourceNotFoundException;
import com.advanceit.clientservice.mapper.ClientMapper;
import com.advanceit.clientservice.repository.ClientRepository;
import com.advanceit.clientservice.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.advanceit.clientservice.async.config.RabbitMQConfigBinding.ACCOUNT_EXCHANGE;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final MessagePublisherService messagePublisher;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper, MessagePublisherService messagePublisher) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
        this.messagePublisher = messagePublisher;
    }

    @Override
    public ClientDTO createClient(ClientDTO client) {
        ClientDTO clientObject = clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(client)));
        messagePublisher.publishMessage(
                ACCOUNT_EXCHANGE,
                "account.create",
                clientObject,
                OperationType.CREATE,
                "client-service"
        );
        return clientObject;
    }

    @Override
    public ClientDTO updateClient(Long id, ClientDTO clientDTO) {
        ClientDTO client = getClientById(id);
        client.setAddress(clientDTO.getAddress());
        return clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(client)));
    }

    @Override
    public void deleteClient(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with id: " + id));

        return clientMapper.toDTO(client);
    }

    @Override
    public List<ClientDTO> getAllClients() {
        List<Client> clients = clientRepository.findAll();
        return clientMapper.toDTOList(clients);
    }

    @Override
    public ClientDTO getClientById(String clientId) {
        Client client = clientRepository.findByClientId(clientId)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found with identifier: " + clientId));
        return clientMapper.toDTO(client);
    }
}
