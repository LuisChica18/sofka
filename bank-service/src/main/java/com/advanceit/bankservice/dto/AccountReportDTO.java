package com.advanceit.bankservice.dto;

import java.math.BigDecimal;
import java.util.List;

public class AccountReportDTO {
    private String clientName;
    private String accountNumber;
    private String accountType;
    private boolean status;
    private BigDecimal currentBalance;
    private List<TransactionReportDTO> transactions;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(BigDecimal currentBalance) {
        this.currentBalance = currentBalance;
    }

    public List<TransactionReportDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionReportDTO> transactions) {
        this.transactions = transactions;
    }
}