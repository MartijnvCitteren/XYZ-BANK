package com.xyz_bank.onboarding.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.xyz_bank.onboarding.exception.LoginException;
import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBuffered;
import com.xyz_bank.onboarding.rest.dto.LoginRequestDto;
import com.xyz_bank.onboarding.rest.dto.LoginResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
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
        String password = findPassword(loginRequestDto);
        if(!loginRequestDto.password().equals(password)) {
            throw new LoginException("This account does not exist or you use invalid login credentials");
        }
        return new LoginResponseDto(createToken());
    }

    private String createToken() {
        Algorithm algorithm = Algorithm.HMAC256("xyzb");
        try {
            return JWT.create().withIssuer("XYZ-BANK").sign(algorithm);
        } catch (JWTCreationException e) {
            log.error("Error during JWT creation" + e.getMessage());
            throw new LoginException("Something unexpected happened during login, please try again");
        }
    }

    private String findPassword(LoginRequestDto loginRequestDto) {
        if(existingUsernamesAndPasswords.isEmpty()){
            existingUsernamesAndPasswords.putAll(customerRepositoryBuffered.getAllUsernamesAndPasswords());
        }
        return existingUsernamesAndPasswords.get(loginRequestDto.username());
    }

}
