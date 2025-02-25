package com.bootcamp.btmsaccounts.mapper;

import com.bootcamp.btmsaccounts.dto.query.PassiveAccountQueryResponseDTO;
import com.bootcamp.btmsaccounts.model.Account;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MapperQueryAccount {

    @Qualifier("passiveMapper")
    private final ModelMapper accountMapper;

    public PassiveAccountQueryResponseDTO convertToAccountQueryDTO(Account account) {
        return accountMapper.map(account, PassiveAccountQueryResponseDTO.class);
    }
}
