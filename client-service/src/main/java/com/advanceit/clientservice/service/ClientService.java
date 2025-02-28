package com.advanceit.clientservice.service;

import com.advanceit.clientservice.dto.ClientDTO;

import java.util.List;

public interface ClientService {
    ClientDTO createClient(ClientDTO client);
    ClientDTO updateClient(Long id, ClientDTO client);
    void deleteClient(Long id);
    ClientDTO getClientById(Long id);
    List<ClientDTO> getAllClients();
    ClientDTO getClientById(String clientId);
}