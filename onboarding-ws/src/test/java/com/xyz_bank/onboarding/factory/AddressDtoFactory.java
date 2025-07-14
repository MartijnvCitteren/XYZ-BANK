package com.xyz_bank.onboarding.factory;

import com.xyz_bank.onboarding.model.enums.Country;
import com.xyz_bank.onboarding.rest.dto.AddressDto;

public class AddressDtoFactory {
    public static AddressDto.AddressDtoBuilder createAddressDto() {
        return AddressDto.builder()
                .country(Country.THE_NETHERLANDS)
                .city("Amsterdam")
                .street("Main Street")
                .zipcode("1234AB")
                .houseNumber("42");
    }
}
