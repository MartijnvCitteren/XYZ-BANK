package com.xyz_bank.onboarding.rest.dto;

import com.xyz_bank.onboarding.model.enums.Country;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AddressDto(
        @NotNull(message = "Country can't be empty, please let us know in which country you live")
        Country country,

        @NotBlank(message = "City has to contain at least on letter")
        String city,

        @NotBlank(message = "Street has to contain at least on letter")
        String street,

        @NotBlank(message = "Zipcode has to contain at least on letter")
        String zipcode,

        @NotBlank(message = "House number has to contain at least on number or letter")
        String houseNumber

) {
}
