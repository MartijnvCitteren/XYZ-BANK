package com.xyz_bank.onboarding.rest.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
public record RegistrationRequestDto(
        @NotBlank(message="Username has to contain at least one letter")
        String username,

        @NotBlank(message="First has to contain at least one letter")
        String firstname,

        @NotBlank(message="Lastname has to contain at least one letter")
        String lastname,

        @Email(message ="Your email address is invalid, please double check if it's correct")
        @NotBlank(message = "Email has to contain a valid email and cant be empty")
        String email,

        @DateTimeFormat(pattern = "dd/MM/yyyy")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Date must be in format dd/MM/yyyy")
        @NotBlank(message = "Date of Birth has to contain a valid date of birth and cant be empty")
        String dateOfBirth,

        @Valid
        @NotNull
        AddressDto address,

        @Valid
        @NotNull
        AccountDto account
) {
}
