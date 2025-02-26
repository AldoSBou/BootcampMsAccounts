package com.bootcamp.btmsaccounts.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreditContRequestDTO {

    private String productId;
    private String customerId;
    private BigDecimal creditLimit;
    private String accountIdentifier;
    private String cardNumber;
    private LocalDate expiryDate;
    private String cardType;
}
