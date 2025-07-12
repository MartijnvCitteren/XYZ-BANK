package com.xyz_bank.onboarding.rest;

import com.xyz_bank.onboarding.rest.dto.LoginRequestDto;
import com.xyz_bank.onboarding.rest.dto.LoginResponseDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponseDto;
import com.xyz_bank.onboarding.service.CustomerRegistrationService;
import com.xyz_bank.onboarding.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    private final CustomerRegistrationService customerRegistrationService;
    private final CustomerService customerService;

    @Operation(summary = "Register a new customer and create bank account", description =
            "REST API to register a new Customer, create a unique IBAN, create default password")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "HTTP Status CREATED"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(hidden = true))), @ApiResponse(responseCode = "400", description =
            "Invalid " + "request", content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponseDto> register(
            @Valid @RequestBody RegistrationRequestDto registrationRequestDto) {

        RegistrationResponseDto response = customerRegistrationService.register(registrationRequestDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Login customer", description = "REST API that produces a JWToken by a correct login " +
            "credentials Customer, create a unique IBAN, create default password")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "HTTP Status OK"),
            @ApiResponse(responseCode = "500", description = "HTTP Status Internal Server Error", content =
            @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "401", description = "Unauthorized",
                         content = @Content(schema = @Schema(hidden = true)))})
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        LoginResponseDto response = customerService.login(loginRequestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


}
