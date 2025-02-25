package com.bootcamp.btmsaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "accounts")
public class Account {

    @Id
    @EqualsAndHashCode.Include
    private String id;

    @Field("productId")
    private String productId;

    @Field("customerId")
    private String customerId; // **Referencia al CLIENTE**

    @Field("accountNumber") // Identificador ÚNICO de la cuenta (Número de Cuenta o Código de Crédito)
    private String accountNumber;

    @Field("accountCreationDate")
    private LocalDate accountCreationDate;

    @Field("accountStatus")
    private String accountStatus; // Estado de la cuenta (ej. "ACTIVE", "INACTIVE", "CLOSED")

    // **Datos ESPECÍFICOS de la CUENTA (varían por cliente e instancia)**

    @Field("accountBalance") // Saldo para cuentas pasivas
    private BigDecimal accountBalance;

    @Field("creditLimit") // Límite de crédito para cuentas activas
    private BigDecimal creditLimit;

    @Field("availableCredit") // Crédito disponible para cuentas activas
    private BigDecimal availableCredit;

    // **Campos específicos para subtipos de productos activos (Tarjetas de Crédito) -  A nivel de CUENTA si es necesario (límite de tarjeta individualizado, etc.)**

    @Field("cardNumber") // Número de tarjeta (podría ser a nivel de cuenta si es necesario individualizar)
    private String cardNumber;

    @Field("expiryDate") // Fecha de expiración (podría ser a nivel de cuenta si es necesario individualizar)
    private Date expiryDate;

    @Field("cardType") // Tipo de tarjeta (podría ser a nivel de cuenta si es necesario individualizar)
    private String cardType;
}
