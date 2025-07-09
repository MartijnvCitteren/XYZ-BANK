package com.xyz_bank.onboarding.rest.dto;

import com.xyz_bank.onboarding.model.enums.Country;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record RegistrationRequest(
        @NotBlank(message="Username has to contain at least one letter")
        String username,

        @Pattern(regexp = "^(?=.*[!@_\\-$])(?=\\S{6,})[\\w!@_\\-$]+$", message = "Password must be at least 6 " +
                "+characters long and contain at least one special character (!@_-$)")
        String password,

        @NotBlank(message="First has to contain at least one letter")
        String firstname,

        @NotBlank(message="Lastname has to contain at least one letter")
        String lastname,

        @Email(message ="Your email adres is invalid, please double check if it's correct")
        String email,

        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/([0-9]{4})$", message ="Date must be in " +
                "format dd/MM/YYYY")
        String dateOfBirth,

        @NotEmpty(message = "Country can't be empty, please let us know in which country you live")
        Country country,

        @NotBlank(message = "City has to contain at least on letter")
        String city,

        @NotBlank(message = "Street has to contain at least on letter")
        String street,

        @NotBlank(message = "Zipcode has to contain at least on letter")
        String zipcode,

        @NotBlank(message = "City has to contain at least on number or letter")
        String houseNumber
) {
}
