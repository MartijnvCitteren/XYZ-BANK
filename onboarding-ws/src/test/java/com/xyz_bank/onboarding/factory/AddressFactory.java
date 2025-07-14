package com.xyz_bank.onboarding.factory;

import com.xyz_bank.onboarding.model.Address;
import com.xyz_bank.onboarding.model.enums.Country;

import java.util.UUID;

public class AddressFactory {
    public static Address.AddressBuilder createAddress() {
        return Address.builder()
                .id(UUID.randomUUID())
                .country(Country.THE_NETHERLANDS)
                .city("Amsterdam")
                .street("Main Street")
                .zipCode("1234AB")
                .houseNumber("42");
    }
}