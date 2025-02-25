package com.bootcamp.btmsaccounts.dto.query;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassiveAccountQueryResponseDTO {

    private String id;
    private String productId;
    private String customerId;
    private String accountNumber;
    private String accountStatus;
    private BigDecimal accountBalance;
}
