package com.bootcamp.btmsaccounts.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Document(collection = "debit_card")
public class DebitCard {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Field("cardNumber")
    private String cardNumber;
    @Field("expiryDate")
    private String expiryDate;
    @Field("cvv")
    private String cvv;
    @Field("cardType")
    private String cardType;
    @Field("principalAccountId")
    private String principalAccountId;
    @Field("secondaryAccount")
    private List<SecondaryAccount> secondaryAccount;
    @Field
    private String cardStatus;
    @Field
    private String affiliationDate;
    @Field
    private String channel;
    @Field
    private String customerId;
}
