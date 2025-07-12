package com.xyz_bank.onboarding.rest.dto;

import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Currency;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AccountOverviewDto(
        String accountNumber,
        AccountType accountType,
        BigDecimal balance,
        Currency currency
) {
}
