package com.advanceit.clientservice.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.advanceit.clientservice.async.service.MessagePublisherService;
import com.advanceit.clientservice.dto.ClientDTO;
import com.advanceit.clientservice.entity.Client;
import com.advanceit.clientservice.exception.ResourceNotFoundException;
import com.advanceit.clientservice.mapper.ClientMapper;
import com.advanceit.clientservice.repository.ClientRepository;
import com.advanceit.clientservice.service.impl.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @Mock
    private MessagePublisherService messagePublisherService;

    private ClientServiceImpl clientService;

    private ClientDTO clientDTO;
    private Client client;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientServiceImpl(clientRepository, clientMapper, messagePublisherService);

        clientDTO = new ClientDTO(1L, "John Doe", "M", 18,
                "1717171717", "123 Main St", "099999",
                "CLI001", "password", true);
        client = new Client();
        client.setId(1L);
        client.setName("John Doe");
        client.setGender("M");
        client.setAge(18);
        client.setDni("1717171717");
        client.setAddress("123 Main St");
        client.setPhone("099999");
        client.setClientId("CLI001");
        client.setPassword("password");
        client.setStatus(true);
    }

    @Test
    void testCreateClient() {
        // Given
        when(clientMapper.toEntity(clientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        // When
        ClientDTO result = clientService.createClient(clientDTO);

        // Then
        assertNotNull(result);
        assertEquals(clientDTO.getId(), result.getId());
        assertEquals(clientDTO.getName(), result.getName());
        assertEquals(clientDTO.getAddress(), result.getAddress());

        verify(clientRepository, times(1)).save(client);
        verify(clientMapper, times(1)).toEntity(clientDTO);
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void testUpdateClient() {
        ClientDTO updatedClientDTO = new ClientDTO(1L, "John Doe", "M", 18,
                "1717171717", "123 Main St", "099999",
                "CLI001", "password", true);
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toEntity(updatedClientDTO)).thenReturn(client);
        when(clientRepository.save(client)).thenReturn(client);
        when(clientMapper.toDTO(client)).thenReturn(updatedClientDTO);

        ClientDTO result = clientService.updateClient(1L, updatedClientDTO);

        assertNotNull(result);
        assertEquals(updatedClientDTO.getAddress(), result.getAddress());

        verify(clientRepository, times(1)).findById(1L);
        verify(clientRepository, times(1)).save(client);
        verify(clientMapper, times(1)).toEntity(updatedClientDTO);
        verify(clientMapper, times(2)).toDTO(client);
    }

    @Test
    void testDeleteClient() {
        Long clientId = 1L;

        clientService.deleteClient(clientId);

        verify(clientRepository, times(1)).deleteById(clientId);
    }

    @Test
    void testGetClientById() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        // When
        ClientDTO result = clientService.getClientById(1L);

        // Then
        assertNotNull(result);
        assertEquals(clientDTO.getId(), result.getId());
        assertEquals(clientDTO.getName(), result.getName());
        assertEquals(clientDTO.getAddress(), result.getAddress());

        verify(clientRepository, times(1)).findById(1L);
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void testGetClientById_ClientNotFound() {
        // Given
        when(clientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(1L);
        });
        assertEquals("Client not found with id: 1", exception.getMessage());
    }

    @Test
    void testGetAllClients() {
        // Given
        List<Client> clients = Arrays.asList(client);
        List<ClientDTO> clientDTOs = Arrays.asList(clientDTO);
        when(clientRepository.findAll()).thenReturn(clients);
        when(clientMapper.toDTOList(clients)).thenReturn(clientDTOs);

        // When
        List<ClientDTO> result = clientService.getAllClients();

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(clientDTO.getId(), result.get(0).getId());
        assertEquals(clientDTO.getName(), result.get(0).getName());

        verify(clientRepository, times(1)).findAll();
        verify(clientMapper, times(1)).toDTOList(clients);
    }

    @Test
    void testGetClientByClientId() {
        // Given
        String clientId = "abc123";
        when(clientRepository.findByClientId(clientId)).thenReturn(Optional.of(client));
        when(clientMapper.toDTO(client)).thenReturn(clientDTO);

        // When
        ClientDTO result = clientService.getClientById(clientId);

        // Then
        assertNotNull(result);
        assertEquals(clientDTO.getId(), result.getId());
        assertEquals(clientDTO.getName(), result.getName());

        verify(clientRepository, times(1)).findByClientId(clientId);
        verify(clientMapper, times(1)).toDTO(client);
    }

    @Test
    void testGetClientByClientId_ClientNotFound() {
        // Given
        String clientId = "abc123";
        when(clientRepository.findByClientId(clientId)).thenReturn(Optional.empty());

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            clientService.getClientById(clientId);
        });
        assertEquals("Client not found with identifier: abc123", exception.getMessage());
    }
}
