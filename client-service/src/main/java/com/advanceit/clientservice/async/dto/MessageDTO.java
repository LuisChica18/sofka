package com.advanceit.clientservice.async.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO<T> implements Serializable {
    private OperationType operationType;
    private T payload;
    private String sourceService;
}