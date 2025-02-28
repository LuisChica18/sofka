package com.advanceit.bankservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionReportDTO {
    private LocalDateTime date;
    private String typeTransaction;
    private BigDecimal amount;
    private BigDecimal balance;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}