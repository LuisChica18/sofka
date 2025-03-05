package com.advanceit.bankservice.service.impl;

import com.advanceit.bankservice.async.dto.ClientDTO;
import com.advanceit.bankservice.async.listener.ClientMessageListener;
import com.advanceit.bankservice.client.ClientClient;
import com.advanceit.bankservice.dto.AccountDTO;
import com.advanceit.bankservice.entity.Account;
import com.advanceit.bankservice.entity.Transaction;
import com.advanceit.bankservice.enums.MovementTypeEnum;
import com.advanceit.bankservice.exception.ResourceNotFoundException;
import com.advanceit.bankservice.mapper.AccountMapper;
import com.advanceit.bankservice.repository.AccountRepository;
import com.advanceit.bankservice.service.AccountService;
import com.advanceit.bankservice.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final AccountMapper accountMapper;
    private final ClientMessageListener messageListener;
    private final ClientClient clientServiceClient;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionService transactionService, AccountMapper accountMapper, ClientMessageListener messageListener, ClientClient clientServiceClient) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.accountMapper = accountMapper;
        this.messageListener = messageListener;
        this.clientServiceClient = clientServiceClient;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public AccountDTO save(AccountDTO account) throws IllegalArgumentException {
        if (accountRepository.findByAccountNumber(account.getAccountNumber()).isPresent())
            throw new IllegalArgumentException("duplicate account number");

        Account savedAccount = accountRepository.save(accountMapper.toEntity(account));
        Transaction transaction = buildInitialTransaction(savedAccount);
        transactionService.registerTransaction(transaction, true);
        AccountDTO accountDTO = accountMapper.toDTO(savedAccount);
        accountDTO.setClient(messageListener.getClientList()
                .stream()
                .filter(item -> Objects.equals(item.getId(), account.getClient().getId()))
                .findFirst().orElseThrow(() ->
                        new IllegalArgumentException("No found Client Id : " + account.getClient().getId())
                ));
        return accountDTO;
    }

    @Override
    @Transactional(value = Transactional.TxType.REQUIRES_NEW)
    public AccountDTO updateAccount(Long id, AccountDTO account) {
        AccountDTO accountDTO = getAccount(id);
        if (accountDTO.getInitialBalance().compareTo(account.getInitialBalance()) != 0)
            throw new IllegalArgumentException("Change initial balance with original value, no process request");

        account.setId(id);
        Account savedAccount = accountRepository.save(accountMapper.toEntity(account));
        AccountDTO savedAccountDTO = accountMapper.toDTO(savedAccount);
        savedAccountDTO.setClient(messageListener.getClientList()
                .stream()
                .filter(item -> Objects.equals(item.getId(), account.getClient().getId()))
                .findFirst().orElseThrow(() ->
                        new IllegalArgumentException("No found Client Id : " + account.getClient().getId())
                ));
        return savedAccountDTO;
    }

    @Override
    public void deleteAccount(Long id) {
        AccountDTO account = getAccount(id);
        account.setStatus(false);
        accountRepository.save(accountMapper.toEntity(account));
    }

    @Override
    public AccountDTO getAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        ClientDTO clientDTO = clientServiceClient.getClientById(account.getClientId());
        AccountDTO accountDTO = accountMapper.toDTO(account);
        accountDTO.setClient(clientDTO);
        return accountDTO;
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        List<AccountDTO> accountDTOS = new ArrayList<>();
        List<Account> accounts = accountRepository.findAll();
        accounts.forEach(account -> {
            ClientDTO clientDTO = clientServiceClient.getClientById(account.getClientId());
            AccountDTO accountDTO = accountMapper.toDTO(account);
            accountDTO.setClient(clientDTO);
            accountDTOS.add(accountDTO);
        });

        return accountDTOS;
    }

    @Override
    public AccountDTO findByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        ClientDTO clientDTO = clientServiceClient.getClientById(account.getClientId());
        AccountDTO accountDTO = accountMapper.toDTO(account);
        accountDTO.setClient(clientDTO);
        return accountDTO;
    }

    private Transaction buildInitialTransaction(Account account) {
        Transaction transaction = new Transaction();
        transaction.setAmount(account.getInitialBalance());
        transaction.setTypeTransaction(MovementTypeEnum.DEPOSIT);
        transaction.setAccount(account);
        transaction.setBalance(account.getInitialBalance());
        return transaction;
    }
}
