package com.bootcamp.btmsaccounts.service;

import com.bootcamp.btmsaccounts.dto.AccountCommissionsDTO;
import com.bootcamp.btmsaccounts.dto.AccountTransferRequestDTO;
import com.bootcamp.btmsaccounts.dto.AccountTransferResponseDTO;
import com.bootcamp.btmsaccounts.dto.AccountsConsolidateResponseDTO;
import com.bootcamp.btmsaccounts.model.Account;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface IAccountService extends IGenericService<Account,String> {

    Flux<Account> getAccountsByCustomer(String idCustomer);
    Mono<Account> saveAccount(Account account);
    Mono<Void> updateAccountBalance(String accountId, String customerId, Double balance);
    Mono<AccountTransferResponseDTO> savingTransferLocalPost(Mono<AccountTransferRequestDTO> requestDTO);
    Mono<AccountsConsolidateResponseDTO> allProductsByCustomerId(String customerId);
    Mono<AccountCommissionsDTO> accountCommissions(String accountId, String startDate, String endDate);
}
