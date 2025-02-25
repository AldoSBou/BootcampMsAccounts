package com.bootcamp.btmsaccounts.infrastructure.iwebapi;

import com.bootcamp.btmsaccounts.client.model.PassiveProductClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IProductApi {
    public Mono<PassiveProductClient> getProductInformation(String productServiceUri, String productId);
    public Flux<PassiveProductClient> getAllProductsInformation(String productServiceUri);

}
