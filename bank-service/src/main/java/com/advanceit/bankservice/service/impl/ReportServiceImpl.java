package com.advanceit.bankservice.service.impl;

import com.advanceit.bankservice.dto.AccountReportDTO;
import com.advanceit.bankservice.dto.TransactionReportDTO;
import com.advanceit.bankservice.entity.Account;
import com.advanceit.bankservice.entity.Transaction;
import com.advanceit.bankservice.repository.AccountRepository;
import com.advanceit.bankservice.repository.TransactionRepository;
import com.advanceit.bankservice.service.ReportService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportServiceImpl implements ReportService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public ReportServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<AccountReportDTO> generateAccountReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account -> createAccountReport(account, startDate, endDate))
                .collect(Collectors.toList());
    }

    private AccountReportDTO createAccountReport(Account account, LocalDateTime startDate, LocalDateTime endDate) {
        AccountReportDTO reportDTO = new AccountReportDTO();
        reportDTO.setAccountNumber(account.getAccountNumber());
        reportDTO.setAccountType(account.getAccountType());
        reportDTO.setCurrentBalance(account.getInitialBalance());

        List<Transaction> transactions = transactionRepository
                .findByAccountIdAndDateBetween(account.getId(), startDate, endDate);

        reportDTO.setTransactions(transactions.stream()
                .map(this::mapToTransactionDTO)
                .collect(Collectors.toList()));

        return reportDTO;
    }

    private TransactionReportDTO mapToTransactionDTO(Transaction transaction) {
        TransactionReportDTO dto = new TransactionReportDTO();
        dto.setDate(transaction.getDate());
        dto.setTypeTransaction(transaction.getTypeTransaction());
        dto.setAmount(transaction.getAmount());
        dto.setBalance(transaction.getBalance());
        return dto;
    }
}