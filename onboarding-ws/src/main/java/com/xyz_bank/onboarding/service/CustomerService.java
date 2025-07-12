package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBuffered;
import com.xyz_bank.onboarding.rest.dto.LoginRequestDto;
import com.xyz_bank.onboarding.rest.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {
    private final CustomerRepositoryBuffered customerRepositoryBuffered;
    private Map<String, String> existingUsernamesAndPasswords = Collections.synchronizedMap(new HashMap<>());





    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        if(existingUsernamesAndPasswords.containsKey(loginRequestDto.username())) {
            if(existingUsernamesAndPasswords.get(loginRequestDto.username()).equals(loginRequestDto.password())) {

            }
        }
        return null;
    }
}
