package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBufferd;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {
    private final CustomerRepositoryBufferd customerRepositoryBufferd;

    public RegistrationResponseDto register(RegistrationRequestDto registrationRequestDto) {
        return RegistrationResponseDto.builder().username("Hoi").password("123").iban("ik werk").build();
    }
}
