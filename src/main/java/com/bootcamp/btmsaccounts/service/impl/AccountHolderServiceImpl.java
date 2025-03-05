package com.bootcamp.btmsaccounts.service.impl;

import com.bootcamp.btmsaccounts.model.AccountHolder;
import com.bootcamp.btmsaccounts.repository.IAccountHolderRepository;
import com.bootcamp.btmsaccounts.repository.IGenericRepository;
import com.bootcamp.btmsaccounts.service.IAccountHolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AccountHolderServiceImpl extends GenericServiceImpl<AccountHolder, String> implements IAccountHolderService {

    private final IAccountHolderRepository _repository;

    @Override
    protected IGenericRepository<AccountHolder, String> getRepository() {
        return _repository;
    }
}
