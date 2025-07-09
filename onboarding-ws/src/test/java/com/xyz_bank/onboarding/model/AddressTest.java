package com.xyz_bank.onboarding.model;

import com.xyz_bank.onboarding.factory.AddressFactory;
import com.xyz_bank.onboarding.model.enums.Country;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AddressTest {

    @Test
    void givenAddressWithSameFields_whenEquals_returnTrue() {
        //given
        var address1 = AddressFactory.createAddress().build();
        var address2 = AddressFactory.createAddress().build();

        //when & then
        assertEquals(address1, address2);
    }

    @Test
    void givenAddressWithDifferentFields_whenEquals_thenReturnFalse() {
        //given
        var address1 = AddressFactory.createAddress().build();
        var address2 = AddressFactory.createAddress().country(Country.BELGIUM).city("Brussels").build();

        //when & then
        assertNotEquals(address1, address2);
    }

    @Test
    void givenAddressWithSlightlyDifferentHouseNumber_whenEquals_thenReturnFalse() {
        //given
        var address1 = AddressFactory.createAddress().build();
        var address2 = AddressFactory.createAddress().houseNumber("42a").build();

        //when & then
        assertNotEquals(address1, address2);
    }

    @Test
    void givenAddressWithSlightlyDifferentZipCode_whenEquals_thenReturnFalse() {
        //given
        var address1 = AddressFactory.createAddress().build();
        var address2 = AddressFactory.createAddress().zipCode("1234AC").build();

        //when & then
        assertNotEquals(address1, address2);
    }
}
