package com.xyz_bank.onboarding.rest.controller;

import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponseDto;
import com.xyz_bank.onboarding.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> register(
            @Valid @RequestBody RegistrationRequestDto registrationRequestDto) {

        RegistrationResponseDto response = customerService.register(registrationRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

}
