package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.api.ReportingApiDelegate;
import com.bootcamp.btmsaccounts.dto.AccountCommissionsDTO;
import com.bootcamp.btmsaccounts.dto.AccountsConsolidateResponseDTO;
import com.bootcamp.btmsaccounts.service.IAccountService;
import com.bootcamp.btmsaccounts.service.ICreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/accounts/reporting")
public class AccountReportingController implements ReportingApiDelegate {

    private final IAccountService accountService;

    @GetMapping("products/{customerId}")
    public Mono<ResponseEntity<AccountsConsolidateResponseDTO>> reportingCustomerIdConsolidateGet(@PathVariable String customerId,
                                                                                           ServerWebExchange exchange){
        return accountService.allProductsByCustomerId(customerId)
                .map(e -> {
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(e);
                });
    }

    @GetMapping(value = "/{accountId}/commission")
    public Mono<ResponseEntity<AccountCommissionsDTO>> reportingAccountIdCommissionGet(@PathVariable String accountId,
                                                                                       String startDate,
                                                                                       String endDate,
                                                                                       ServerWebExchange exchange){
        return accountService.accountCommissions(accountId,startDate,endDate)
                .map(e -> ResponseEntity.ok().body(e));
    }

}
