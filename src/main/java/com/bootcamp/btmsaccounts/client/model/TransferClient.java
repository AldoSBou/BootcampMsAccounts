package com.bootcamp.btmsaccounts.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransferClient {

    private String accountId;
    private String movementType;
    private Double amount;
    private String movementDate;
    private String description;
    private String channel;
    private Double commission;

}
