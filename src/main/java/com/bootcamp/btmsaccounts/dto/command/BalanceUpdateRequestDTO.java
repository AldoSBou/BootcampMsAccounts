package com.bootcamp.btmsaccounts.dto.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BalanceUpdateRequestDTO {

    private String customerId;
    private Double balance;
    //private String transactionType;
}
