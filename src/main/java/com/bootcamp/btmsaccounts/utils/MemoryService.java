package com.bootcamp.btmsaccounts.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class MemoryService implements IMemoryService {

    private final ReactiveRedisTemplate<String, Object> redisTemplate;

    private final ObjectMapper objectMapper; // Inyección de dependencia

    @Override
    public <T> Mono<T> getValue(String key, TypeReference<T> typeReference) {
        return redisTemplate.opsForValue().get(key)
                .flatMap(value -> {
                    try {
                        T deserializedValue = objectMapper.readValue(objectMapper.writeValueAsString(value), typeReference);
                        return Mono.just(deserializedValue);
                    } catch (Exception e) {
                        return Mono.error(new RuntimeException("Error al deserializar el valor de Redis", e));
                    }
                });
    }

    @Override
    public Mono<Boolean> saveValue(String key, Object value) {
        return redisTemplate.opsForValue().set(key, value);
    }
}
