package com.advanceit.bankservice.service;

import com.advanceit.bankservice.entity.Account;

import java.util.List;

public interface AccountService {
    Account save(Account account);
    Account updateAccount(Long id, Account account);
    void deleteAccount(Long id);
    Account getAccount(Long id);
    List<Account> getAllAccounts();
    Account findByAccountNumber(String accountNumber);
}