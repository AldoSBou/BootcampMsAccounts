package com.bootcamp.btmsaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "credit_card_transactions")
public class CreditCardTransaction {

    private String id;
    private String description;
    private BigDecimal amount;
    private String creditCardNumber;
    private String transactionStatus;
    private String createdAt;
    private String updatedAt;
}
