package com.bootcamp.btmsaccounts.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import reactor.core.publisher.Mono;

public interface IMemoryService {
    <T> Mono<T> getValue(String key, TypeReference<T> typeReference);
    Mono<Boolean> saveValue(String key, Object value);
}
