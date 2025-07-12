package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.iban.DutchIbanGenerator;
import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.enums.Currency;
import com.xyz_bank.onboarding.rest.dto.AccountDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Log4j2
@RequiredArgsConstructor
public class AccountService {
    private final DutchIbanGenerator dutchIbanGenerator;

    public Account createAccount(AccountDto accountDto) {
        String iban = dutchIbanGenerator.generateIban();
        return new Account(iban, accountDto.accountType(), Currency.EURO, new BigDecimal("0.00"));
    }
}
