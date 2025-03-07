package com.bootcamp.btmsaccounts.controller;


import com.bootcamp.btmsaccounts.api.SavingApiDelegate;
import com.bootcamp.btmsaccounts.dto.*;
import com.bootcamp.btmsaccounts.dto.command.BalanceUpdateRequestDTO;
import com.bootcamp.btmsaccounts.mapper.MapperAccount;
import com.bootcamp.btmsaccounts.model.Account;
import com.bootcamp.btmsaccounts.service.IAccountService;
import com.bootcamp.btmsaccounts.service.ICreditCardService;
import com.bootcamp.btmsaccounts.service.ICreditService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController implements SavingApiDelegate {

    private final MapperAccount mapperAccount;
    private final IAccountService accountService;
    private final ICreditService creditService;
    private final ICreditCardService creditCardService;

    @PostMapping("/passive-account")
    public Mono<ResponseEntity<PassiveAccountCreationDTO>> saveAccount(@Valid @RequestBody PassiveAccountCreationDTO account, final ServerHttpRequest request) {

        return accountService.saveAccount(mapperAccount.convertToDocument(account))
                .map(mapperAccount::convertToAccountDTO)
                .map(result -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/").concat(result.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result)
                ).defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping("/customer/{id}")
    public Mono<ResponseEntity<Flux<PassiveAccountCreationDTO>>> getAccountsByCustomerId(@PathVariable("id") String customerId) {
        Flux<PassiveAccountCreationDTO> accountsFlux = accountService.getAccountsByCustomer(customerId)
                .map(mapperAccount::convertToAccountDTO);
        return Mono.just(ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(accountsFlux))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/balance")
    public Mono<ResponseEntity<Void>> updateAccountBalance(@Valid @RequestBody BalanceUpdateRequestDTO request, @PathVariable("id") String id) {
        return accountService.updateAccountBalance(id, request.getCustomerId(), request.getBalance())
                .thenReturn(ResponseEntity.accepted().<Void>build())
                .onErrorResume(e -> Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).<Void>build()));
    }


    @Override
    @GetMapping(value = "/saving/{accountId}/balance")
    public Mono<ResponseEntity<BalanceResponseDTO>> savingAccountIdBalanceGet(@PathVariable("accountId") String accountId, ServerWebExchange exchange) {
        return  accountService.findById(accountId)
                .map(e -> {
                    BalanceResponseDTO responseDTO = new BalanceResponseDTO(BigDecimal.valueOf(e.getAccountBalance()));
                    return ResponseEntity.ok().body(responseDTO);
                });
    }


    @Override
    @PostMapping("/saving/transfer-local")
    public Mono<ResponseEntity<AccountTransferResponseDTO>> savingTransferLocalPost(@RequestBody Mono<AccountTransferRequestDTO> transferLocalRequestDTO,
                                                                           ServerWebExchange exchange){
            return accountService.savingTransferLocalPost(transferLocalRequestDTO)
                    .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Account>> getAccountById(@PathVariable("id") String id){
        return accountService.findById(id)
                .map(e -> ResponseEntity.ok().body(e));
    }
}

