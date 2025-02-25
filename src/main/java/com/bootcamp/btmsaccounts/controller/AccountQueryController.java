package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.dto.query.PassiveAccountQueryResponseDTO;
import com.bootcamp.btmsaccounts.mapper.MapperQueryAccount;
import com.bootcamp.btmsaccounts.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/accounts/queries")
public class AccountQueryController {

    private final IAccountService accountService;
    private final MapperQueryAccount mapperAccount;

    @GetMapping("/{accountId}/customer/{customerId}/information")
    Mono<ResponseEntity<PassiveAccountQueryResponseDTO>> getAccountByCustomerId(@PathVariable("accountId") String accountId, @PathVariable("customerId") String customerId) {
        return accountService.findById(accountId)
                .filter(acc -> acc.getCustomerId().equals(customerId))
                .map(mapperAccount::convertToAccountQueryDTO)
                .map(e ->
                        ResponseEntity.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(e)).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
