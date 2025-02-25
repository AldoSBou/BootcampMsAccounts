package com.bootcamp.btmsaccounts.repository;

import com.bootcamp.btmsaccounts.model.Account;
import reactor.core.publisher.Flux;

public interface IAccountRepository extends IGenericRepository<Account, String> {
   Flux<Account> findByCustomerId(String customerId);
}
