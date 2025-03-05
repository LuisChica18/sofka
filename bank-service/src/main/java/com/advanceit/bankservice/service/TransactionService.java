package com.advanceit.bankservice.service;

import com.advanceit.bankservice.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction registerTransaction(Transaction transaction, boolean initialTransaction);
    List<Transaction> getAllTransactions();
    Transaction getTransaction(Long id);
    void deleteTransaction(Long id);
}
