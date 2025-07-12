package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBuffered;
import com.xyz_bank.onboarding.rest.dto.LoginRequestDto;
import com.xyz_bank.onboarding.rest.dto.LoginResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private CustomerRepositoryBuffered customerRepository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void givenValidLoginCredentials_whenLogin_thenReturnToken() {
        //given
        String username = "username";
        String password = "password";
        LoginRequestDto login = new LoginRequestDto(username, password);
        when(customerRepository.getAllUsernamesAndPasswords()).thenReturn(Map.of(username, password));

        //when
        LoginResponseDto result = customerService.login(login);

        //then
        assertNotNull(result);
    }

}