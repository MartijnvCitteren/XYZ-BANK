package com.xyz_bank.onboarding.rest.dto;

import com.xyz_bank.onboarding.model.enums.AccountType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AccountDto(
    @NotNull(message = "AccountType can't be empty, please let us know what accounttype you want")
    AccountType accountType
){}
