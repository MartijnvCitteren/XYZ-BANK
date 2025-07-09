package com.xyz_bank.onboarding.model;

import com.xyz_bank.onboarding.factory.AccountFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AccountTest {

    private static final String IBAN_1 = "NL05XYZB1234567890";
    private static final String IBAN_2 = "NL05XYZB1234566666";

    @Test
    void givenAccountWithSameIBAN_whenEquals_returnTrue() {
        //given
        var account1 = AccountFactory.createAccount().iban(IBAN_1).build();
        var account2 = AccountFactory.createAccount().iban(IBAN_1).build();

        //when & then
        assertEquals(account1, account2);
    }

    @Test
    void givenDifferentIbanButSameID_whenEquals_thenReturnFalse() {
        //given
        UUID id = UUID.randomUUID();
        var account1 = AccountFactory.createAccount().id(id).iban(IBAN_1).build();
        var account2 = AccountFactory.createAccount().iban(IBAN_2).build();

        //when & then
        assertNotEquals(account1, account2);
    }

}