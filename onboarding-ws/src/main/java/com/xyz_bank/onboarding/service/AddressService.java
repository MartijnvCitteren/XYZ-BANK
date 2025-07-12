package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.model.Address;
import com.xyz_bank.onboarding.rest.dto.AddressDto;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    public Address createAddress(AddressDto addressDto){
        Address address = new Address();
        address.setCountry(addressDto.country());
        address.setCity(addressDto.city());
        address.setStreet(addressDto.street());
        address.setZipCode(addressDto.zipcode());
        address.setHouseNumber(addressDto.houseNumber());
        return address;
    }
}
