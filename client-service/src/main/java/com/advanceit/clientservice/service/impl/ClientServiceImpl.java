package com.advanceit.clientservice.service.impl;

import com.advanceit.clientservice.dto.ClientDTO;
import com.advanceit.clientservice.entity.Client;
import com.advanceit.clientservice.exception.ResourceNotFoundException;
import com.advanceit.clientservice.mapper.ClientMapper;
import com.advanceit.clientservice.repository.ClientRepository;
import com.advanceit.clientservice.service.ClientService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientServiceImpl(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    @Override
    public ClientDTO createClient(ClientDTO client) {
        return clientMapper.toDTO(clientRepository.save(clientMapper.toEntity(client)));
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
