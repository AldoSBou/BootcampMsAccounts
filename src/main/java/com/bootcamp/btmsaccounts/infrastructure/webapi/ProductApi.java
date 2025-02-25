package com.bootcamp.btmsaccounts.infrastructure.webapi;

import com.bootcamp.btmsaccounts.client.model.PassiveProductClient;
import com.bootcamp.btmsaccounts.infrastructure.iwebapi.IProductApi;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ProductApi implements IProductApi {

    private final WebClient.Builder clientBuilder;

    @Override
    public Mono<PassiveProductClient> getProductInformation(String productServiceUri, String productId) {
        WebClient webClient = clientBuilder.build();

        return webClient.get()
                .uri(productServiceUri + "/api/products/passive/{id}",productId)
                .retrieve()
                .bodyToMono(PassiveProductClient.class)
                .onErrorResume(throwable -> Mono.empty());
    }

    @Override
    public Flux<PassiveProductClient> getAllProductsInformation(String productServiceUri) {
        WebClient webClient = clientBuilder.build();
        return webClient.get()
                .uri(productServiceUri + "/api/products/passive")
                .retrieve()
                .bodyToFlux(PassiveProductClient.class)
                .map(result -> {
                    System.out.println("Resulto Products: " +result.toString());
                    return result;
                })
                .onErrorComplete().log();
    }
}
