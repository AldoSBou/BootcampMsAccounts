package com.bootcamp.btmsaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountHolder {
    private String customerId;
    private String holderType;
}
