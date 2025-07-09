package com.xyz_bank.onboarding.model.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum Country {
    BELGIUM("BE"),
    THE_NETHERLANDS("NL"),
    GERMANY("DE");

    private final String countryCode;
}
