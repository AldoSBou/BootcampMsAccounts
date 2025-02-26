package com.bootcamp.btmsaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "credits")
public class Credit {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String productId;
    private LocalDate creationDate;
    private String accountNumber; //Campo futuro, para validar que vaya asociada a una cuenta de ahorro
    private String status;
    private String customerId;
    private double creditLimit; // Si se trata de tarjeta de crédito (Monto Original de la TC)
    private double availableCredit; // Si se trata de tarjeta de crédito (Monto actual de la linea de credito)
    private String creditSubType; // Credito/Prestamo(Personal, Empresarial), Tarjeta de credito
    private String idCreditCard; // Solo para TC
    private String lastTransactionId; // Solo TC - Campo para validar la ultima transaccion de la TC
}
