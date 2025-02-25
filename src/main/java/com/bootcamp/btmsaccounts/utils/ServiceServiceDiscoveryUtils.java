package com.bootcamp.btmsaccounts.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ServiceServiceDiscoveryUtils implements IServiceDiscoveryUtils {

    private final ReactiveDiscoveryClient discoveryClient;

    public Mono<String> getDiscoveryInstances(String instace){
        return discoveryClient.getInstances(instace)
                .next()
                .map(instance -> instance.getUri().toString());
    }
}
