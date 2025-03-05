package com.advanceit.bankservice.service;

import com.advanceit.bankservice.dto.AccountDTO;

import java.util.List;

public interface AccountService {
    AccountDTO save(AccountDTO account);
    AccountDTO updateAccount(Long id, AccountDTO account);
    void deleteAccount(Long id);
    AccountDTO getAccount(Long id);
    List<AccountDTO> getAllAccounts();
    AccountDTO findByAccountNumber(String accountNumber);
}