package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.api.CreditContractsApiDelegate;
import com.bootcamp.btmsaccounts.dto.CreditContractRequestDTO;
import com.bootcamp.btmsaccounts.dto.CreditContractResponseDTO;
import com.bootcamp.btmsaccounts.dto.command.CreditContRequestDTO;
import com.bootcamp.btmsaccounts.service.ICreditService;
import lombok.RequiredArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/accounts/credit-card")
public class AccountCreditController implements CreditContractsApiDelegate {

    private final ICreditService _service;

    @PostMapping
    public Mono<ResponseEntity<CreditContractResponseDTO>> creditContractsPost(@RequestBody Mono<CreditContractRequestDTO>  creditContractRequestDTO,
                                                                        ServerWebExchange exchange) {

        return _service.saveTCCredit(creditContractRequestDTO)
                .map(ResponseEntity::ok);

    }
}
