package com.xyz_bank.onboarding.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.xyz_bank.onboarding.exception.LoginException;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBuffered;
import com.xyz_bank.onboarding.rest.dto.AccountOverviewDto;
import com.xyz_bank.onboarding.rest.dto.LoginRequestDto;
import com.xyz_bank.onboarding.rest.dto.LoginResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {
    private final CustomerRepositoryBuffered customerRepositoryBuffered;
    private final Cache<String, String> inlogCache = Caffeine.newBuilder()
            .expireAfterWrite(2, TimeUnit.HOURS)
            .maximumSize(500)
            .build();
    private final Map<String, String> existingUsernamesAndPasswords = Collections.synchronizedMap(new HashMap<>());

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        String password = getRegisteredPassword(loginRequestDto);
        if (!loginRequestDto.password().equals(password)) {
            throw new LoginException("This account does not exist or you use invalid login credentials");
        }
        String token = createToken();
        inlogCache.put(token, loginRequestDto.username());
        return new LoginResponseDto(token);
    }

    public AccountOverviewDto getOverview(String token) {
        String username = inlogCache.getIfPresent(stripToken(token));
        if (username == null) {
            throw new LoginException("Please login again");
        }

        Customer customer = customerRepositoryBuffered.findCustomerByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(username));

        return AccountOverviewDto.builder()
                .accountNumber(customer.getOriginalAccountNumber())
                .accountType(customer.getAccountType())
                .balance(customer.getBalance())
                .currency(customer.getCurrency()).build();
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

    private String getRegisteredPassword(LoginRequestDto loginRequestDto) {
        if (existingUsernamesAndPasswords.isEmpty()) {
            existingUsernamesAndPasswords.putAll(customerRepositoryBuffered.getAllUsernamesAndPasswords());
        }
        return existingUsernamesAndPasswords.get(loginRequestDto.username());
    }

    private String stripToken(String token) {
        String bearerPrefix = "Bearer ";
        if (token == null || !token.startsWith(bearerPrefix)) {
            throw new LoginException("Invalid token format");
        }
        return token.substring(bearerPrefix.length());
    }

}
