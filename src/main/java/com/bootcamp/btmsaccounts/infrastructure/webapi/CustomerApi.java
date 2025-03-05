package com.bootcamp.btmsaccounts.infrastructure.webapi;

import com.bootcamp.btmsaccounts.client.model.CustomerClient;
import com.bootcamp.btmsaccounts.infrastructure.iwebapi.ICustomerApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CustomerApi implements ICustomerApi {

    private final WebClient.Builder clientBuilder;

    @Override
    public Mono<CustomerClient> getCustomerInformation(String customerServiceUri, String customerId) {
        WebClient webClient = clientBuilder.build();
        return webClient.get()
                .uri(customerServiceUri + "/api/customers/{id}",customerId)
                .retrieve()
                .bodyToMono(CustomerClient.class)
                .log();
    }
}
