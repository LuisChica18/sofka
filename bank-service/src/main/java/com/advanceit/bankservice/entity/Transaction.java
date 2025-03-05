package com.advanceit.bankservice.entity;

import com.advanceit.bankservice.enums.MovementTypeEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime date;
    private MovementTypeEnum typeTransaction;
    private BigDecimal amount;
    private BigDecimal balance;

    @ManyToOne
    private Account account;
}
