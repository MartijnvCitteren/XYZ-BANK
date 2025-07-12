package com.xyz_bank.onboarding.rest.dto;

import lombok.Builder;

@Builder
public record RegistrationResponseDto(
        String username,
        String password,
        String iban
) {
}
