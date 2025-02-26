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
@Document(collection = "loans")
public class Loan {

    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String productId;
    private LocalDate creationDate;
    private String status;
    private String customerId;
    private BigDecimal creditAmount; //Solo para prestamos
    private String creditSubType; // Credito(Personal, Empresarial), Tarjeta de credito
    private String paymentDay;
    private BigDecimal installmentAmount;
    private Integer installmentsCount;
}
