package com.xyz_bank.onboarding.rest.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequestDto(
        @NotNull (message = "Username can't be null")
        String username,
        @NotNull  (message = "Password can't be null")
        String password
) {}
