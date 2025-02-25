package com.bootcamp.btmsaccounts.service;

import com.bootcamp.btmsaccounts.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IAccountService extends IGenericService<Account,String> {

    Flux<Account> getAccountsByCustomer(String idCustomer);
    Mono<Account> saveAccount(Account account);
    Mono<Void> updateAccountBalance(String accountId, String customerId, BigDecimal balance);
}
