package com.advanceit.bankservice.mapper;

import com.advanceit.bankservice.dto.AccountDTO;
import com.advanceit.bankservice.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AccountMapper {

    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(source = "client.id", target = "clientId")
    Account toEntity(AccountDTO accountDTO) ;

    AccountDTO toDTO(Account account);

    List<AccountDTO> toDTOList(List<Account> accounts);
    List<Account> toEntityList(List<AccountDTO> accountDTOS);

}
