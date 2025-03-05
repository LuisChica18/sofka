package com.advanceit.bankservice.dto;

import com.advanceit.bankservice.enums.MovementTypeEnum;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TransactionReportDTO {
    private LocalDateTime date;
    private MovementTypeEnum typeTransaction;
    private BigDecimal amount;
    private BigDecimal balance;

}