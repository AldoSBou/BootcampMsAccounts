package com.bootcamp.btmsaccounts.infrastructure.iwebapi;

import com.bootcamp.btmsaccounts.client.model.CustomerClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICustomerApi {
    Mono<CustomerClient> getCustomerInformation(String customerServiceUri, String customerId);
}
