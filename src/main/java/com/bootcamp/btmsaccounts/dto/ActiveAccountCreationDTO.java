package com.bootcamp.btmsaccounts.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.Date;

public class ActiveAccountCreationDTO {

    @NotBlank(message = "El productId es obligatorio")
    private String productId;

    @NotBlank(message = "El customerId es obligatorio")
    private String customerId;

    @NotBlank(message = "El accountNumber es obligatorio")
    private String accountIdentifier;

    @NotNull(message = "El creditLimit es obligatorio")
    @Positive(message = "El creditLimit debe ser un valor positivo")
    private BigDecimal creditLimit;

    // Campos específicos para Tarjetas de Crédito (opcionales, dependiendo del subtipo de producto activo)
    private String cardNumber;
    private Date expiryDate;
    private String cardType;

    // No incluimos availableCredit, creationDate ni status, ya que estos se calcularán/establecerán en el servicio.
    // availableCredit se inicializa igual al creditLimit al crear la cuenta activa.
}
