package com.advanceit.clientservice.mapper;

import com.advanceit.clientservice.dto.ClientDTO;
import com.advanceit.clientservice.entity.Client;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-27T22:57:28-0500",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.4 (Oracle Corporation)"
)
@Component
public class ClientMapperImpl implements ClientMapper {

    @Override
    public Client toEntity(ClientDTO clientDTO) {
        if ( clientDTO == null ) {
            return null;
        }

        Client client = new Client();

        client.setId( clientDTO.getId() );
        client.setName( clientDTO.getName() );
        client.setGender( clientDTO.getGender() );
        client.setAge( clientDTO.getAge() );
        client.setDni( clientDTO.getDni() );
        client.setAddress( clientDTO.getAddress() );
        client.setPhone( clientDTO.getPhone() );
        client.setClientId( clientDTO.getClientId() );
        client.setPassword( clientDTO.getPassword() );
        client.setStatus( clientDTO.isStatus() );

        return client;
    }

    @Override
    public ClientDTO toDTO(Client client) {
        if ( client == null ) {
            return null;
        }

        ClientDTO clientDTO = new ClientDTO();

        clientDTO.setId( client.getId() );
        clientDTO.setName( client.getName() );
        clientDTO.setGender( client.getGender() );
        clientDTO.setAge( client.getAge() );
        clientDTO.setDni( client.getDni() );
        clientDTO.setAddress( client.getAddress() );
        clientDTO.setPhone( client.getPhone() );
        clientDTO.setClientId( client.getClientId() );
        clientDTO.setPassword( client.getPassword() );
        clientDTO.setStatus( client.isStatus() );

        return clientDTO;
    }

    @Override
    public void updateClientFromDto(ClientDTO dto, Client client) {
        if ( dto == null ) {
            return;
        }

        client.setId( dto.getId() );
        client.setName( dto.getName() );
        client.setGender( dto.getGender() );
        client.setAge( dto.getAge() );
        client.setDni( dto.getDni() );
        client.setAddress( dto.getAddress() );
        client.setPhone( dto.getPhone() );
        client.setClientId( dto.getClientId() );
        client.setPassword( dto.getPassword() );
        client.setStatus( dto.isStatus() );
    }

    @Override
    public List<ClientDTO> toDTOList(List<Client> clients) {
        if ( clients == null ) {
            return null;
        }

        List<ClientDTO> list = new ArrayList<ClientDTO>( clients.size() );
        for ( Client client : clients ) {
            list.add( toDTO( client ) );
        }

        return list;
    }

    @Override
    public List<Client> toEntityList(List<ClientDTO> clientDTOs) {
        if ( clientDTOs == null ) {
            return null;
        }

        List<Client> list = new ArrayList<Client>( clientDTOs.size() );
        for ( ClientDTO clientDTO : clientDTOs ) {
            list.add( toEntity( clientDTO ) );
        }

        return list;
    }
}
