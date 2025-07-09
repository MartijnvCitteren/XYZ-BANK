package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBufferd;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequest;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {
    private final CustomerRepositoryBufferd customerRepositoryBufferd;

    public RegistrationResponse register(RegistrationRequest registrationRequest) {
        return RegistrationResponse.builder().username("Hoi").password("123").iban("ik werk").build();
    }
}
