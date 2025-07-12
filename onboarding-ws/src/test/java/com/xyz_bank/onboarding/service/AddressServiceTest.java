package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.factory.AddressDtoFactory;
import com.xyz_bank.onboarding.model.Address;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class AddressServiceTest {
    @InjectMocks
    private AddressService addressService;

    @Test
    void givenValidAddressDto_whenGetAddress_thenReturnAddress() {
        //given
        var addressDto = AddressDtoFactory.createAddressDto().build();

        //when
        Address address = addressService.createAddress(addressDto);

        //then
        assertEquals(addressDto.country(), address.getCountry());
        assertEquals(addressDto.city(), address.getCity());
        assertEquals(addressDto.street(), address.getStreet());
        assertEquals(addressDto.zipcode(), address.getZipCode());
        assertEquals(addressDto.houseNumber(), address.getHouseNumber());
    }

}