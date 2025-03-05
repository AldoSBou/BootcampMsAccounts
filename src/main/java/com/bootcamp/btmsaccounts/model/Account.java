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
import java.util.List;

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
    private String accountCreationDate;

    @Field("accountStatus")
    private String accountStatus; // Estado de la cuenta (ej. "ACTIVE", "INACTIVE", "CLOSED")

    // **Datos ESPECÍFICOS de la CUENTA (varían por cliente e instancia)**

    @Field("accountBalance") // Saldo para cuentas pasivas
    private Double accountBalance;

    private List<AccountHolder> holders;

}
