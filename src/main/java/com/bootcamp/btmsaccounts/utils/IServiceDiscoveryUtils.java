package com.bootcamp.btmsaccounts.utils;

import reactor.core.publisher.Mono;

public interface IServiceDiscoveryUtils {
    Mono<String> getDiscoveryInstances(String instace);
}
