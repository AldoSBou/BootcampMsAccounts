package com.bootcamp.btmsaccounts.service.impl;

import com.bootcamp.btmsaccounts.model.CreditCard;
import com.bootcamp.btmsaccounts.repository.ICreditCardRepository;
import com.bootcamp.btmsaccounts.repository.IGenericRepository;
import com.bootcamp.btmsaccounts.service.ICreditCardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreditCardServiceImpl extends GenericServiceImpl<CreditCard, String> implements ICreditCardService {

    private final ICreditCardRepository _repository;

    @Override
    protected IGenericRepository<CreditCard, String> getRepository() {
        return _repository;
    }

}
