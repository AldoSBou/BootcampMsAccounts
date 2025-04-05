package com.bootcamp.btmsaccounts.dto;

import com.bootcamp.btmsaccounts.model.SecondaryAccount;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DebitCardDto {
    private String id;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String cardType;
    private String principalAccountId;
    private List<SecondaryAccount> secondaryAccount;
    private String cardStatus;
    private String affiliationDate;
    private String channel;
    private String customerId;
}