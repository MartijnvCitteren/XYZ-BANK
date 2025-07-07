package com.xyz_bank.onboarding.factory;

import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Currency;

import java.math.BigDecimal;
import java.util.UUID;

public class AccountFactory {
    public static Account.AccountBuilder createAccount() {
        return Account.builder()
                .id(UUID.randomUUID())
                .iban("NL05XYZB0038479539")
                .type(AccountType.CHECKING)
                .currency(Currency.EURO)
                .balance(BigDecimal.valueOf(0.00));
    }
}
