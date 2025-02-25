package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.dto.PassiveAccountCreationDTO;
import com.bootcamp.btmsaccounts.dto.command.BalanceUpdateRequestDTO;
import com.bootcamp.btmsaccounts.mapper.MapperAccount;
import com.bootcamp.btmsaccounts.service.IAccountService;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final IAccountService accountService;
    private final MapperAccount mapperAccount;

    @PostMapping("passive-account")
    Mono<ResponseEntity<PassiveAccountCreationDTO>> saveAccount(@Valid @RequestBody PassiveAccountCreationDTO account, final ServerHttpRequest request) {

        return accountService.saveAccount(mapperAccount.convertToDocument(account))
                .map(mapperAccount::convertToAccountDTO)
                .map(result -> ResponseEntity.created(URI.create(request.getURI().toString().concat("/").concat(result.getId())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(result)
                ).defaultIfEmpty(ResponseEntity.notFound().build())
                .onErrorReturn(ResponseEntity.badRequest().build());
    }

    @GetMapping("/customer/{id}")
    Mono<ResponseEntity<Flux<PassiveAccountCreationDTO>>> getAccountsByCustomerId(@PathVariable("id") String customerId) {
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
}
