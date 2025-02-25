package com.bootcamp.btmsaccounts.client.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassiveProductClient {

    private String id;
    private ProductTypeClient productType;
    private String status;
    private String productSubType;
    private Integer monthlyTransactionLimit;
    private BigDecimal maintenanceFee;
}
