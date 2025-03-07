package com.bootcamp.btmsaccounts.service;

import com.bootcamp.btmsaccounts.dto.DebitCardAffiliationRequestDTO;
import com.bootcamp.btmsaccounts.dto.DebitCardAffiliationResponseDTO;
import com.bootcamp.btmsaccounts.model.DebitCard;
import reactor.core.publisher.Mono;

public interface IDebitCardService extends IGenericService<DebitCard,String>{
    Mono<DebitCardAffiliationResponseDTO> saveDebitCard(Mono<DebitCardAffiliationRequestDTO> debitCardAffiliationRequestDTO);
}
