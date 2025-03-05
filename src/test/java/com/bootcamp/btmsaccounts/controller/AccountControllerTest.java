package com.bootcamp.btmsaccounts.controller;

import com.bootcamp.btmsaccounts.dto.PassiveAccountCreationDTO;
import com.bootcamp.btmsaccounts.mapper.MapperAccount;
import com.bootcamp.btmsaccounts.model.Account;
import com.bootcamp.btmsaccounts.service.IAccountService;
import com.bootcamp.btmsaccounts.service.ICreditCardService;
import com.bootcamp.btmsaccounts.service.ICreditService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import com.bootcamp.btmsaccounts.dto.command.BalanceUpdateRequestDTO;

import java.math.BigDecimal;

@WebFluxTest(controllers = AccountController.class) // Replace with your actual Controller class name
public class AccountControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private MapperAccount mapperAccount;
    @MockitoBean
    private IAccountService accountService;
    @MockitoBean
    private ICreditService creditService;
    @MockitoBean
    private ICreditCardService creditCardService;
    @MockitoBean
    private WebProperties.Resources resources;

    private BalanceUpdateRequestDTO balanceRequest;
    private PassiveAccountCreationDTO accountCreationDTO;
    private Account account;

    @BeforeEach
    public void init(){
        MockitoAnnotations.openMocks(this); //Lo que viene abajo va a ser inicializado

        balanceRequest = new BalanceUpdateRequestDTO();
        balanceRequest.setBalance(100.00);
        balanceRequest.setCustomerId("123459218s9asd");

        Mockito.when(mapperAccount.convertToDocument(accountCreationDTO)).thenReturn(account);
    }

    @Test
    void updateAccountBalance_validRequest_returnsAccepted() {
        // Arrange
        String accountId = "1213123412321";

        Mockito.when(accountService.updateAccountBalance(accountId, balanceRequest.getCustomerId(), balanceRequest.getBalance()))
                .thenReturn(Mono.empty()); // Mock successful service call

        // Act & Assert
        webTestClient.post()
                .uri("/api/accounts/" + accountId + "/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceRequest)
                .exchange()
                .expectStatus().isAccepted(); // Status 202 Accepted
    }

   /* @Test
    void updateAccountBalance_invalidRequest_returnsBadRequest() {
        // Arrange
        String accountId = "testAccountId";
        BalanceUpdateRequestDTO requestDTO = new BalanceUpdateRequestDTO();
        requestDTO.setCustomerId(""); // Invalid - CustomerId is Blank
        requestDTO.setBalance(BigDecimal.valueOf(100.00));

        // Act & Assert
        webTestClient.post()
                .uri("/" + accountId + "/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isBadRequest(); // Status 400 Bad Request - Due to @Valid violation
    }

    @Test
    void updateAccountBalance_serviceError_returnsBadRequest() {
        // Arrange
        String accountId = "testAccountId";
        BalanceUpdateRequestDTO requestDTO = new BalanceUpdateRequestDTO();
        requestDTO.setCustomerId("testCustomerId");
        requestDTO.setBalance(BigDecimal.valueOf(100.00));

        Mockito.when(accountService.updateAccountBalance(accountId, requestDTO.getCustomerId(), requestDTO.getBalance()))
                .thenReturn(Mono.error(new RuntimeException("Service failed"))); // Mock service returning error

        // Act & Assert
        webTestClient.post()
                .uri("/" + accountId + "/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestDTO)
                .exchange()
                .expectStatus().isBadRequest(); // Status 400 Bad Request - Due to onErrorResume in controller
    }*/
}