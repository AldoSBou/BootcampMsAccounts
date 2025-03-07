package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.api.CreditCardApiDelegate;
import com.bootcamp.btmsaccounts.dto.*;
import com.bootcamp.btmsaccounts.service.ICreditCardService;
import com.bootcamp.btmsaccounts.service.ICreditCardTransactionService;
import com.bootcamp.btmsaccounts.service.ICreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/accounts/credit-card")
public class CreditController implements CreditCardApiDelegate {

    private final ICreditService _creditService;
    private final ICreditCardService _creditCardService;
    private final ICreditCardTransactionService _creditCardTraService;

    @PostMapping
    public Mono<ResponseEntity<CreditContractResponseDTO>> creditContractsPost(@RequestBody Mono<CreditContractRequestDTO> creditContractRequestDTO,
                                                                               ServerWebExchange exchange) {

        return _creditService.saveTCCredit(creditContractRequestDTO)
                .map(ResponseEntity::ok);

    }

    @Override
    @GetMapping("/{cardNumber}/balance")
    public Mono<ResponseEntity<BalanceResponseDTO>> creditCardCardNumberBalanceGet(@PathVariable("cardNumber") String cardNumber, ServerWebExchange exchange) {
        return _creditCardService.findByCardNumber(cardNumber)
                .flatMap(e -> _creditService.findByIdCreditCard(e.getId()))
                .log()
                .map(e -> ResponseEntity.ok().body(new BalanceResponseDTO(e.getAvailableCredit())))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @PostMapping("/{cardNumber}/transaction")
    public Mono<ResponseEntity<CreditCardTransactionResponseDTO>> creditCardCardNumberTransactionsPost(@PathVariable("cardNumber") String cardNumber, @RequestBody
                                                                                                       Mono<CreditCardTransactionRequestDTO> creditCardTransactionRequestDTO,
                                                                                                       ServerWebExchange exchange) {
        return _creditCardTraService.creditCardTransaction(cardNumber, creditCardTransactionRequestDTO)
                .map(ResponseEntity::ok);

    }
}
