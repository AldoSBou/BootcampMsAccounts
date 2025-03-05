package com.bootcamp.btmsaccounts.service.impl;

import com.bootcamp.btmsaccounts.dto.CreditCardTransactionRequestDTO;
import com.bootcamp.btmsaccounts.dto.CreditCardTransactionResponseDTO;
import com.bootcamp.btmsaccounts.model.CreditCardTransaction;
import com.bootcamp.btmsaccounts.repository.ICreditCardRepository;
import com.bootcamp.btmsaccounts.repository.ICreditCardTransactionRepository;
import com.bootcamp.btmsaccounts.repository.IGenericRepository;
import com.bootcamp.btmsaccounts.service.ICreditCardTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class CreditCardTransactionServiceImpl extends GenericServiceImpl<CreditCardTransaction, String> implements ICreditCardTransactionService {

    private final ICreditCardTransactionRepository creditCardTransactionRepository;
    private final ICreditCardRepository creditCardRepository;

    @Override
    protected IGenericRepository<CreditCardTransaction, String> getRepository() {
        return creditCardTransactionRepository;
    }

    @Override
    public Mono<CreditCardTransactionResponseDTO> creditCardTransaction(String cardNumber, Mono<CreditCardTransactionRequestDTO> creditCardTransactionRequestDTO) {

        return  creditCardTransactionRequestDTO
                .flatMap(requestDto ->
                        creditCardRepository.findByCardNumber(cardNumber)
                        .switchIfEmpty(Mono.empty())
                        .flatMap(creditCard -> {

                            // **Validaciones IMPORTANTES aquí antes de crear la transacción:**
                            // 1. Validar fecha de expiración vs fecha actual
                            // 2. Validar CVV (aunque en un sistema real, NUNCA guardes CVV y la validación sería con un servicio externo)
                            // 3. **Validar LÍNEA DE CRÉDITO DISPONIBLE vs. requestDTO.getAmount() - ¡Falta esta validación crucial!**

                            CreditCardTransaction transaction = new CreditCardTransaction();
                            transaction.setAmount(requestDto.getAmount());
                            transaction.setDescription(requestDto.getDescription());
                            transaction.setTransactionStatus("PENDING"); // Estado inicial PENDING hasta validaciones completas
                            transaction.setCreditCardNumber(cardNumber);
                            transaction.setAmount(requestDto.getAmount());
                            transaction.setTransactionStatus("APPROVED");
                            transaction.setCreatedAt(requestDto.getTransactionDate());
                            transaction.setUpdatedAt(requestDto.getTransactionDate());

                            return creditCardTransactionRepository.save(transaction)
                                    .map(savedTransaction -> {
                                        CreditCardTransactionResponseDTO responseDTO = new CreditCardTransactionResponseDTO();
                                        responseDTO.setTransactionId(savedTransaction.getId());
                                        responseDTO.setTransactionStatus(savedTransaction.getTransactionStatus());
                                        return responseDTO;
                                    });
                        }));
    }
}