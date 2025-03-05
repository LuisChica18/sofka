package com.advanceit.bankservice.service.impl;

import com.advanceit.bankservice.async.dto.ClientDTO;
import com.advanceit.bankservice.client.ClientClient;
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

@Service
public class ReportServiceImpl implements ReportService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final ClientClient clientServiceClient;

    public ReportServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository, ClientClient clientServiceClient) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.clientServiceClient = clientServiceClient;
    }

    @Override
    public List<AccountReportDTO> generateAccountReport(LocalDateTime startDate, LocalDateTime endDate) {
        List<Account> accounts = accountRepository.findAll();

        return accounts.stream()
                .map(account -> createAccountReport(account, startDate, endDate))
                .toList();
    }

    private AccountReportDTO createAccountReport(Account account, LocalDateTime startDate, LocalDateTime endDate) {

        ClientDTO clientDTO = clientServiceClient.getClientById(account.getClientId());

        AccountReportDTO reportDTO = new AccountReportDTO();
        reportDTO.setClientName(clientDTO.getName());
        reportDTO.setAccountNumber(account.getAccountNumber());
        reportDTO.setAccountType(account.getAccountType().name());
        reportDTO.setCurrentBalance(account.getInitialBalance());
        reportDTO.setStatus(account.isStatus());

        List<Transaction> transactions = transactionRepository
                .findByAccountIdAndDateBetween(account.getId(), startDate, endDate);

        reportDTO.setTransactions(transactions.stream()
                .map(this::mapToTransactionDTO)
                .toList());

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