package com.advanceit.clientservice.async.dto;

import com.advanceit.clientservice.async.enums.AccountTypeEnum;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AccountDTO {

    private Long id;

    @NotBlank(message = "Account number is required")
    private String accountNumber;

    @NotBlank(message = "Account Type is required")
    private AccountTypeEnum accountType;

    @Min(value = 200, message = "Initial balance must be greater than or equal to 18")
    private BigDecimal initialBalance;

    @NotNull(message = "Status is required")
    private boolean status;

    private Long clientId;

}
