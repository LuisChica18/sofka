package com.advanceit.bankservice.service.impl;

import com.advanceit.bankservice.entity.Account;
import com.advanceit.bankservice.entity.Transaction;
import com.advanceit.bankservice.enums.MovementTypeEnum;
import com.advanceit.bankservice.exception.InsufficientBalanceException;
import com.advanceit.bankservice.exception.ResourceNotFoundException;
import com.advanceit.bankservice.repository.AccountRepository;
import com.advanceit.bankservice.repository.TransactionRepository;
import com.advanceit.bankservice.service.TransactionService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public Transaction registerTransaction(Transaction transaction, boolean initalTransaction) {
        Account account = accountRepository.findByAccountNumber(transaction.getAccount().getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found"));

        if (!account.isStatus())
            throw new IllegalArgumentException("Account not active");

        BigDecimal currentBalance = account.getInitialBalance();
        BigDecimal amount = transaction.getAmount();
        BigDecimal newBalance;
        if (initalTransaction)
            newBalance =  currentBalance;
        else
            newBalance = currentBalance.add(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientBalanceException("Balance not available");
        }

        transaction.setDate(LocalDateTime.now());
        transaction.setBalance(newBalance);
        transaction.setTypeTransaction(amount.compareTo(BigDecimal.ZERO) >= 0 ? MovementTypeEnum.DEPOSIT : MovementTypeEnum.WITHDRAWAL);
        transaction.setAccount(account);

        account.setInitialBalance(newBalance);
        accountRepository.save(account);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    public Transaction getTransaction(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with id: " + id));
    }

    @Override
    public void deleteTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
