package com.bootcamp.btmsaccounts.config;

import com.bootcamp.btmsaccounts.dto.PassiveAccountCreationDTO;
import com.bootcamp.btmsaccounts.dto.query.PassiveAccountResponseDTO;
import com.bootcamp.btmsaccounts.model.Account;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
public class MapperConfig {

    @Bean("defaultMapper")
    public ModelMapper modelMapper() { return new ModelMapper(); }

    @Bean("accountMapper")
    public ModelMapper accountMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.createTypeMap(Account.class, PassiveAccountCreationDTO.class)
                .addMapping(Account::getId, (dest,v) -> dest.setId((String)v))
                .addMapping(Account::getCustomerId, (dest,v) -> dest.setCustomerId((String)v))
                .addMapping(Account::getProductId, (dest,v) -> dest.setProductType((String)v))
                .addMapping(Account::getAccountNumber, (dest,v) -> dest.setAccountNumber((String)v))
                .addMapping(Account::getAccountCreationDate, (dest,v) -> dest.setAccountCreationDate((String) v))
                .addMapping(Account::getAccountStatus, (dest,v) -> dest.setAccountStatus((String) v))
                .addMapping(Account::getAccountBalance, (dest,v) -> dest.setAccountBalance((BigDecimal) v));

        mapper.createTypeMap(PassiveAccountCreationDTO.class, Account.class)
                .addMapping(PassiveAccountCreationDTO::getId, (dest, v) -> dest.setId((String)v))
                .addMapping(PassiveAccountCreationDTO::getCustomerId, (dest, v) -> dest.setCustomerId((String)v))
                .addMapping(PassiveAccountCreationDTO::getProductType, (dest, v) -> dest.setProductId((String)v))
                .addMapping(PassiveAccountCreationDTO::getAccountNumber, (dest, v) -> dest.setAccountNumber((String)v))
                .addMapping(PassiveAccountCreationDTO::getAccountCreationDate, (dest, v) -> dest.setAccountCreationDate((String) v))
                .addMapping(PassiveAccountCreationDTO::getAccountStatus, (dest, v) -> dest.setAccountStatus((String) v))
                .addMapping(PassiveAccountCreationDTO::getAccountBalance, (dest, v) -> dest.setAccountBalance((Double) v));

        return mapper;
    }

    @Bean("passiveMapper")
    public ModelMapper passiveMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        //Lectura
        mapper.createTypeMap(Account.class, PassiveAccountResponseDTO.class)
                .addMapping(Account::getId, (dest,v) -> dest.setId((String)v))
                .addMapping(Account::getCustomerId, (dest,v) -> dest.setCustomerId((String)v))
                .addMapping(Account::getProductId, (dest,v) -> dest.setProductId((String)v))
                .addMapping(Account::getAccountNumber, (dest,v) -> dest.setAccountNumber((String)v))
                .addMapping(Account::getAccountBalance, (dest,v) -> dest.setAccountBalance((BigDecimal) v));
        return mapper;

    }
}
