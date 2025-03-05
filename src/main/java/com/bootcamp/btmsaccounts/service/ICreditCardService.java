package com.bootcamp.btmsaccounts.service;

import com.bootcamp.btmsaccounts.model.CreditCard;
import com.bootcamp.btmsaccounts.service.impl.GenericServiceImpl;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface ICreditCardService extends IGenericService<CreditCard, String> {
    Mono<CreditCard> findByCardNumber(String cardNumber);
}
