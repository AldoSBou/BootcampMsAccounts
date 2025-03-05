package com.bootcamp.btmsaccounts.service.impl;

import com.bootcamp.btmsaccounts.dto.CreditContractDetailsDTO;
import com.bootcamp.btmsaccounts.dto.CreditContractRequestDTO;
import com.bootcamp.btmsaccounts.dto.CreditContractResponseDTO;
import com.bootcamp.btmsaccounts.model.Credit;
import com.bootcamp.btmsaccounts.model.CreditCard;
import com.bootcamp.btmsaccounts.repository.ICreditCardRepository;
import com.bootcamp.btmsaccounts.repository.ICreditRepository;
import com.bootcamp.btmsaccounts.repository.IGenericRepository;
import com.bootcamp.btmsaccounts.service.ICreditCardService;
import com.bootcamp.btmsaccounts.service.ICreditService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl extends GenericServiceImpl<Credit, String> implements ICreditService {

    private final ICreditRepository creditRepository;
    private final ICreditCardRepository creditCardService;

    @Override
    protected IGenericRepository<Credit, String> getRepository() {
        return creditRepository;
    }

    @Override
    public Mono<CreditContractResponseDTO> saveTCCredit(Mono<CreditContractRequestDTO> creditRequestMono) {
        return creditRequestMono
                .flatMap(creditReq -> { // Process the CreditContractRequestDTO

                    // 1. Create CreditCard object
                    CreditCard creditCard = new CreditCard();
                    creditCard.setCardNumber(String.valueOf(creditReq.getCardNumber())); // Consider proper type conversion and validation
                    creditCard.setCardType(String.valueOf(creditReq.getCardType()));   // Consider proper type conversion and validation
                    creditCard.setExpiryDate(String.valueOf(creditReq.getExpiryDate())); // Consider proper type conversion and validation
                    creditCard.setCvv("548"); // Consider where this CVV value comes from and security implications

                    // 2. Save CreditCard and then create Credit object
                    return creditCardService.save(creditCard)
                            .flatMap(savedCreditCard -> {

                                Credit saveTCCredit = new Credit();
                                saveTCCredit.setProductId(creditReq.getProductId());
                                saveTCCredit.setCreationDate(LocalDate.now());
                                saveTCCredit.setStatus("ACTIVE");
                                saveTCCredit.setCustomerId(creditReq.getCustomerId());
                                saveTCCredit.setCreditLimit(creditReq.getCreditLimit());
                                saveTCCredit.setAvailableCredit(creditReq.getCreditLimit());
                                saveTCCredit.setCreditSubType("CREDIT CARD");
                                saveTCCredit.setIdCreditCard(savedCreditCard.getId());

                                // 4. Save Credit object
                                return creditRepository.save(saveTCCredit);
                            })
                            .map(savedCredit -> {

                                CreditContractResponseDTO responseDTO = new CreditContractResponseDTO();
                                responseDTO.setAccountNumber("1239123821381");

                                CreditContractDetailsDTO detailsDTO = new CreditContractDetailsDTO();
                                detailsDTO.setCustomerId(creditReq.getCustomerId());
                                detailsDTO.setProductId(creditReq.getProductId());
                                detailsDTO.setCreditLimit(creditReq.getCreditLimit());
                                detailsDTO.setAccountStatus("ACTIVE");
                                responseDTO.setContractDetails(detailsDTO);

                                return responseDTO;
                            });
                });
    }

    @Override
    public Mono<Credit> findByIdCreditCard(String idCreditCard) {
        return creditRepository.findByIdCreditCard(idCreditCard);
    }

    @Override
    public Flux<Credit> findByCustomerId(String customerId) {
        return creditRepository.findByCustomerId(customerId);
    }
}
