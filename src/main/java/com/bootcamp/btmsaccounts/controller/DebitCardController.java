package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.api.DebitCardApiDelegate;
import com.bootcamp.btmsaccounts.dto.DebitCardAffiliationRequestDTO;
import com.bootcamp.btmsaccounts.dto.DebitCardAffiliationResponseDTO;
import com.bootcamp.btmsaccounts.service.IDebitCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accounts/debit-card")
public class DebitCardController implements DebitCardApiDelegate {

    private final IDebitCardService debitCardService;

    @PostMapping("/affiliation")
    public Mono<ResponseEntity<DebitCardAffiliationResponseDTO>>
    debitCardAffiliationPost(@RequestBody Mono<DebitCardAffiliationRequestDTO> debitCardAffiliationRequestDTO,
                             ServerWebExchange exchange) {

        return debitCardService.saveDebitCard(debitCardAffiliationRequestDTO)
                .map(e -> ResponseEntity.ok().body(e));

    }

}
