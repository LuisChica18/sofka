package com.advanceit.bankservice.controller;

import com.advanceit.bankservice.entity.Account;
import com.advanceit.bankservice.entity.Transaction;
import com.advanceit.bankservice.service.AccountService;
import com.advanceit.bankservice.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;
    private final TransactionService transactionService;

    public AccountController(AccountService accountService, TransactionService transactionService) {
        this.accountService = accountService;
        this.transactionService = transactionService;
    }

    @PostMapping
    public Account createAccount(@RequestBody Account account) {
        return accountService.save(account);
    }

    @PutMapping("/{id}")
    public Account updateAccount(@PathVariable Long id, @RequestBody Account account) {
        return accountService.updateAccount(id, account);
    }

    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
    }

    @GetMapping("/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/movimientos")
    public ResponseEntity<Transaction> registerTransaction(@RequestBody Transaction transaction) {
        Transaction registeredTransaction = transactionService.registerTransaction(transaction);
        return ResponseEntity.ok(registeredTransaction);
    }
}
