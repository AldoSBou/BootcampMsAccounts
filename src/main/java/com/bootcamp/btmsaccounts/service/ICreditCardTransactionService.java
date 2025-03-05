package com.bootcamp.btmsaccounts.service;

import com.bootcamp.btmsaccounts.dto.CreditCardTransactionRequestDTO;
import com.bootcamp.btmsaccounts.dto.CreditCardTransactionResponseDTO;
import com.bootcamp.btmsaccounts.model.CreditCardTransaction;
import reactor.core.publisher.Mono;

public interface ICreditCardTransactionService extends IGenericService<CreditCardTransaction, String> {
    Mono<CreditCardTransactionResponseDTO> creditCardTransaction(String cardNumber, Mono<CreditCardTransactionRequestDTO> creditCardTransactionRequestDTO);
}
