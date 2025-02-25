package com.bootcamp.btmsaccounts.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IGenericService <T,ID>{
    Mono<T> save(T t);
    Mono<T> update(ID id,T t);
    Mono<Boolean> delete(ID id);
    Flux<T> findAll();
    Mono<T> findById(ID id);
}
