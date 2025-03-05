package com.bootcamp.btmsaccounts.infrastructure.webapi;

import com.bootcamp.btmsaccounts.client.model.PassiveProductClient;
import com.bootcamp.btmsaccounts.client.model.TransferClient;
import com.bootcamp.btmsaccounts.infrastructure.iwebapi.ITransferApi;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransferApi implements ITransferApi {

    private final WebClient.Builder clientBuilder;

    @Override
    public Flux<TransferClient> getAllTransfersByAccountId(String transferUrlApi, String accountId, String startDate, String endDate) {
        WebClient webClient = clientBuilder.build();

        // Construir la URI completa usando UriComponentsBuilder
        UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl(transferUrlApi)
                .path("/api/transfers/account/{accountId}/movements")
                .queryParam("startDate", startDate)
                .queryParam("endDate", endDate)
                .buildAndExpand(accountId);

        return webClient.get()
                .uri(uriComponents.toUri())
                .retrieve()
                .bodyToFlux(TransferClient.class)
                .onErrorMap(e -> {
                    log.error(e.toString());
                    return e;
                });
    }
}
