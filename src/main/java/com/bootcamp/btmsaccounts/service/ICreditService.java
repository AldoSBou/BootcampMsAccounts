package com.bootcamp.btmsaccounts.service;

import com.bootcamp.btmsaccounts.dto.CreditContractRequestDTO;
import com.bootcamp.btmsaccounts.dto.CreditContractResponseDTO;
import com.bootcamp.btmsaccounts.model.Credit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ICreditService extends IGenericService<Credit, String> {
    Mono<CreditContractResponseDTO> saveTCCredit(Mono<CreditContractRequestDTO> credit);
    Mono<Credit> findByIdCreditCard(String idCreditCard);
    Flux<Credit> findByCustomerId(String customerId);
}
