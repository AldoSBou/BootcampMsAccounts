package com.bootcamp.btmsaccounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassiveAccountCreationDTO {

    private String id;

    @NotBlank(message = "El productDefinitionId es obligatorio")
    private String productType;

    @NotBlank(message = "El customerId es obligatorio")
    private String customerId;

    @NotBlank(message = "El accountIdentifier es obligatorio")
    private String accountNumber;

    @NotNull(message = "El balance inicial no puede ser nulo")
    @PositiveOrZero(message = "El balance inicial debe ser positivo o cero")
    private BigDecimal accountBalance; // Balance inicial de la cuenta

    private LocalDate accountCreationDate;

    private String accountStatus;

}
