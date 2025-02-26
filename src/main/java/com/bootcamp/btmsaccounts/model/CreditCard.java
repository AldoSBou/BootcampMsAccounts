package com.bootcamp.btmsaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "credit_card")
public class CreditCard {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardType;
}
