package com.bootcamp.btmsaccounts.repository;

import com.bootcamp.btmsaccounts.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditRepository extends IGenericRepository<Credit, String> {
    Mono<Credit> findByIdCreditCard(String idCreditCard);
    Flux<Credit> findByCustomerId(String customerId);
}
