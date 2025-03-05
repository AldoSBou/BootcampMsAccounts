package com.bootcamp.btmsaccounts.repository;

import com.bootcamp.btmsaccounts.model.CreditCard;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICreditCardRepository extends IGenericRepository<CreditCard, String> {
    Mono<CreditCard> findByCardNumber(String cardNumber);
}
