package com.bootcamp.btmsaccounts.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface IGenericRepository<T,ID> extends ReactiveMongoRepository<T, ID> {
}
