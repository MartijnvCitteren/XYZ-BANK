package com.xyz_bank.onboarding.rest.dto;

import lombok.Builder;

@Builder
public record RegistrationResponse(
        String username,
        String password,
        String iban
) {
}
