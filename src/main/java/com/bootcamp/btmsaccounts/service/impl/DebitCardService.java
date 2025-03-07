package com.bootcamp.btmsaccounts.service.impl;

import com.bootcamp.btmsaccounts.dto.DebitCardAffiliationRequestDTO;
import com.bootcamp.btmsaccounts.dto.DebitCardAffiliationResponseDTO;
import com.bootcamp.btmsaccounts.model.DebitCard;
import com.bootcamp.btmsaccounts.repository.IDebitCardRepository;
import com.bootcamp.btmsaccounts.repository.IGenericRepository;
import com.bootcamp.btmsaccounts.service.IDebitCardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.security.SecureRandom;
import java.time.YearMonth;

@Slf4j
@Service
@RequiredArgsConstructor
public class DebitCardService extends GenericServiceImpl<DebitCard,String> implements IDebitCardService {

    private final IDebitCardRepository repository;
    private final SecureRandom random = new SecureRandom();


    @Override
    protected IGenericRepository<DebitCard, String> getRepository() {
        return repository;
    }

    @Override
    public Mono<DebitCardAffiliationResponseDTO> saveDebitCard(Mono<DebitCardAffiliationRequestDTO> debitCardAffiliationRequestDTO) {
        return debitCardAffiliationRequestDTO
                .doOnNext(e -> System.out.println("Received request: " + e)) // Log de la petición
                .map(e -> {
                    DebitCard document = new DebitCard();
                    document.setPrincipalAccountId(e.getAccountId());
                    document.setChannel(e.getChannel());
                    document.setCustomerId(e.getCustomerId());
                    document.setAffiliationDate(e.getTransactionDate());
                    return document;
                })
                .doOnNext(e -> System.out.println("Transformed to DebitCard: " + e)) // Log de la transformación
                .flatMap(this::generateCard)
                .doOnNext(e -> System.out.println("Generated DebitCard: " + e)) // Log de la tarjeta generada
                .map(e -> {
                    DebitCardAffiliationResponseDTO responseDTO = new DebitCardAffiliationResponseDTO();
                    responseDTO.setDebitCardNumber(e.getCardNumber());
                    responseDTO.setChannel(e.getChannel());
                    responseDTO.setTransactionId(e.getId());
                    return responseDTO;
                })
                .doOnNext(e -> System.out.println("Transformed to response: " + e)) // Log de la respuesta
                .onErrorResume(ex -> {
                    System.err.println("Error in saveDebitCard: " + ex.getMessage());
                    return Mono.error(ex); // Propaga el error
                });
    }

    private Mono<DebitCard> generateCard(DebitCard debitCard) {
        return generateNumberCard("4", 16)
                .zipWith(generarFechaVencimientoReactiva())
                .map(tuple -> {
                    debitCard.setCardNumber(tuple.getT1());
                    debitCard.setExpiryDate(tuple.getT2());
                    debitCard.setCvv(cvvGenerator());
                    debitCard.setCardType("Visa");
                    debitCard.setChannel("APP");
                    return debitCard;
                })
                .flatMap(repository::save);
    }

    private DebitCardAffiliationResponseDTO convertToResponseDto(DebitCard debitCard) {
        DebitCardAffiliationResponseDTO responseDTO = new DebitCardAffiliationResponseDTO();
        responseDTO.setDebitCardNumber(debitCard.getCardNumber());
        responseDTO.setChannel(debitCard.getChannel());
        responseDTO.setTransactionId(debitCard.getId());
        return responseDTO;
    }

    private DebitCard convertToDocument(DebitCardAffiliationRequestDTO debitCardDTO) {
        DebitCard document = new DebitCard();
        document.setPrincipalAccountId(debitCardDTO.getAccountId());
        document.setChannel(debitCardDTO.getChannel());
        document.setCustomerId(debitCardDTO.getCustomerId());
        document.setAffiliationDate(document.getAffiliationDate());
        return document;
    }

    private Mono<String> generateNumberCard(String prefijo, int longitud) {
        return Mono.fromCallable(() -> {
            StringBuilder sb = new StringBuilder(prefijo);
            for (int i = 0; i < longitud - prefijo.length() - 1; i++) {
                sb.append(random.nextInt(10));
            }
            int digitoControl = calcularDigitoControl(sb.toString());
            sb.append(digitoControl);
            return sb.toString();
        });
    }

    private int calcularDigitoControl(String numero) {
        int suma = 0;
        boolean alternar = false;
        for (int i = numero.length() - 1; i >= 0; i--) {
            int n = Integer.parseInt(numero.substring(i, i + 1));
            if (alternar) {
                n *= 2;
                if (n > 9) {
                    n = (n % 10) + 1;
                }
            }
            suma += n;
            alternar = !alternar;
        }
        return (10 - (suma % 10)) % 10;
    }

    private Mono<String> generarFechaVencimientoReactiva() {
        return Mono.fromCallable(() -> {
            int mes = random.nextInt(12) + 1;
            int anio = YearMonth.now().getYear() + random.nextInt(5) + 1;
            return String.format("%02d/%02d", mes, anio % 100);
        });
    }

    private String cvvGenerator() {
        return String.valueOf(random.nextInt(900) + 100);
    }
}