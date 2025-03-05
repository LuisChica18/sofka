package com.advanceit.clientservice.async.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.STRING)
public enum AccountTypeEnum {
    SAVINGS("Ahorros"),
    CHECKING("Corriente");

    private String typeAccount;

    AccountTypeEnum(String typeAccount) {
        this.typeAccount = typeAccount;
    }
}
