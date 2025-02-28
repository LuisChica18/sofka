package com.advanceit.clientservice.mapper;

import com.advanceit.clientservice.dto.ClientDTO;
import com.advanceit.clientservice.entity.Client;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientMapper {

    ClientMapper INSTANCE = Mappers.getMapper(ClientMapper.class);

    Client toEntity(ClientDTO clientDTO) ;

    ClientDTO toDTO(Client client);

    void updateClientFromDto(ClientDTO dto, @MappingTarget Client client);

    List<ClientDTO> toDTOList(List<Client> clients);
    List<Client> toEntityList(List<ClientDTO> clientDTOs);

}
