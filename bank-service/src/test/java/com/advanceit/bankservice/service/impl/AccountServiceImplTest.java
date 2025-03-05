package com.advanceit.bankservice.service.impl;

import com.advanceit.bankservice.async.dto.ClientDTO;
import com.advanceit.bankservice.async.listener.ClientMessageListener;
import com.advanceit.bankservice.client.ClientClient;
import com.advanceit.bankservice.dto.AccountDTO;
import com.advanceit.bankservice.entity.Account;
import com.advanceit.bankservice.entity.Transaction;
import com.advanceit.bankservice.enums.AccountTypeEnum;
import com.advanceit.bankservice.exception.ResourceNotFoundException;
import com.advanceit.bankservice.mapper.AccountMapper;
import com.advanceit.bankservice.repository.AccountRepository;
import com.advanceit.bankservice.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private AccountMapper accountMapper;

    @Mock
    private ClientClient clientServiceClient;

    @Mock
    private ClientMessageListener clientMessageListener;

    @InjectMocks
    private AccountServiceImpl accountService;

    private AccountDTO accountDTO;
    private Account account;
    private ClientDTO clientDTO;

    @BeforeEach
    void setUp() {

        clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("Marianela Montalvo");
        clientDTO.setClientId("1");

        accountDTO = new AccountDTO();
        accountDTO.setId(1L);
        accountDTO.setAccountNumber("0001");
        accountDTO.setAccountType(AccountTypeEnum.SAVINGS);
        accountDTO.setInitialBalance(new BigDecimal("1000"));
        accountDTO.setStatus(true);
        accountDTO.setClient(clientDTO);

        account = new Account();
        account.setId(1L);
        account.setAccountNumber("0001");
        account.setAccountType(AccountTypeEnum.SAVINGS);
        account.setInitialBalance(new BigDecimal("1000"));
        account.setStatus(true);
        account.setClientId(1L);
    }

    @Test
    @DisplayName("Should save account successfully")
    void shouldSaveAccountSuccessfully() {
        when(clientMessageListener.getClientList()).thenReturn(List.of(clientDTO));
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);
        when(transactionService.registerTransaction(any(Transaction.class), eq(true))).thenReturn(new Transaction());

        AccountDTO result = accountService.save(accountDTO);

        assertNotNull(result);
        assertEquals(accountDTO.getAccountNumber(), result.getAccountNumber());
        verify(accountRepository).save(any(Account.class));
        verify(transactionService).registerTransaction(any(Transaction.class), eq(true));
    }

    @Test
    @DisplayName("Should throw exception when saving account with duplicate number")
    void shouldThrowExceptionWhenSavingDuplicateAccount() {
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(account));

        assertThrows(IllegalArgumentException.class, () -> accountService.save(accountDTO));
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    @DisplayName("Should update account successfully")
    void shouldUpdateAccountSuccessfully() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(clientServiceClient.getClientById(1L)).thenReturn(clientDTO);

        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(account);
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);
        when(clientMessageListener.getClientList()).thenReturn(List.of(clientDTO));

        AccountDTO result = accountService.updateAccount(1L, accountDTO);

        assertNotNull(result);
        assertEquals(accountDTO.getId(), result.getId());
        verify(accountRepository).save(any(Account.class));
    }

    @Test
    @DisplayName("Should get account by id successfully")
    void shouldGetAccountByIdSuccessfully() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);

        AccountDTO result = accountService.getAccount(1L);

        assertNotNull(result);
        assertEquals(accountDTO.getId(), result.getId());
    }

    @Test
    @DisplayName("Should throw exception when getting non-existent account")
    void shouldThrowExceptionWhenGettingNonExistentAccount() {

        when(accountRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.getAccount(1L));
    }

    @Test
    @DisplayName("Should get all accounts successfully")
    void shouldGetAllAccountsSuccessfully() {
        List<Account> accounts = Arrays.asList(account);
        when(accountRepository.findAll()).thenReturn(accounts);
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);
        when(clientServiceClient.getClientById(anyLong())).thenReturn(clientDTO);

        List<AccountDTO> result = accountService.getAllAccounts();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(clientServiceClient).getClientById(anyLong());
    }

    @Test
    @DisplayName("Should find account by number successfully")
    void shouldFindAccountByNumberSuccessfully() {
        when(clientServiceClient.getClientById(anyLong())).thenReturn(clientDTO);
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);
        when(clientServiceClient.getClientById(anyLong())).thenReturn(new ClientDTO());

        AccountDTO result = accountService.findByAccountNumber("0001");

        assertNotNull(result);
        assertEquals(accountDTO.getAccountNumber(), result.getAccountNumber());
        verify(clientServiceClient).getClientById(anyLong());
    }

    @Test
    @DisplayName("Should throw exception when finding non-existent account number")
    void shouldThrowExceptionWhenFindingNonExistentAccountNumber() {
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> accountService.findByAccountNumber("0001"));
    }

    @Test
    @DisplayName("Should delete account successfully")
    void shouldDeleteAccountSuccessfully() {
        when(accountRepository.findById(anyLong())).thenReturn(Optional.of(account));
        when(accountMapper.toDTO(any(Account.class))).thenReturn(accountDTO);
        when(accountMapper.toEntity(any(AccountDTO.class))).thenReturn(account);

        accountService.deleteAccount(1L);

        verify(accountRepository).save(any(Account.class));
        assertFalse(accountDTO.isStatus());
    }
}