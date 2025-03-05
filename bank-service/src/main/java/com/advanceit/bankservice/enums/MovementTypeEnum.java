package com.advanceit.bankservice.enums;

public enum MovementTypeEnum {

    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL");

    private String typeMovement;

    MovementTypeEnum(String typeMovement) {
        this.typeMovement = typeMovement;
    }
}
