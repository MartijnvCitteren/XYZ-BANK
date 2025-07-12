package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.exception.IbanGenerationException;
import com.xyz_bank.onboarding.iban.DutchIbanGenerator;
import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Currency;
import com.xyz_bank.onboarding.rest.dto.AccountDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    private static final String IBAN = "NL02XYZB1000567890";
    private static final BigDecimal START_BALANCE = new BigDecimal("0.00");

    @Mock
    private DutchIbanGenerator dutchIbanGenerator;

    @InjectMocks
    private AccountService accountService;

    @Test
    void givenValidAccountDto_whenCreateAccount_thenReturnAccount() {
        //given
        var accountDto = AccountDto.builder().accountType(AccountType.CHECKING).build();
        when(dutchIbanGenerator.generateIban()).thenReturn(IBAN);

        //whem
        Account result = accountService.createAccount(accountDto);

        //then
        verify(dutchIbanGenerator, times(1)).generateIban();
        assertEquals(AccountType.CHECKING, result.getType());
        assertEquals(Currency.EURO, result.getCurrency());
        assertEquals(START_BALANCE, result.getBalance());
    }

    @Test
    void givenDutchIbanGeneratorThrowsException_whenCreateAccount_thenThrowException() {
        //given
        var accountDto = AccountDto.builder().accountType(AccountType.CHECKING).build();
        when(dutchIbanGenerator.generateIban()).thenThrow(IbanGenerationException.class);

        //when & then
        assertThrows(IbanGenerationException.class, () -> accountService.createAccount(accountDto));
    }


}