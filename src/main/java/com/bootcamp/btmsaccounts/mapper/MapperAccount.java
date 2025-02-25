package com.bootcamp.btmsaccounts.mapper;

import com.bootcamp.btmsaccounts.dto.PassiveAccountCreationDTO;
import com.bootcamp.btmsaccounts.model.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperAccount {

    @Qualifier("accountMapper")
    private final ModelMapper accountMapper;

    public PassiveAccountCreationDTO convertToAccountDTO(Account model) { return accountMapper.map(model, PassiveAccountCreationDTO.class); }
    public Account convertToDocument(PassiveAccountCreationDTO dto) { return accountMapper.map(dto, Account.class); }
}
