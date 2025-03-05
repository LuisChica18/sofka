package com.advanceit.bankservice.async.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Min(value = 18, message = "Age must be greater than or equal to 18")
    private int age;

    @NotBlank(message = "DNI is required")
    private String dni;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Client ID is required")
    private String clientId;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Status is required")
    private boolean status;
}
