package com.xyz_bank.onboarding.factory;

import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.rest.dto.AccountDto;
import com.xyz_bank.onboarding.rest.dto.AddressDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;

public class RegistrationRequestDtoFactory {
    public static RegistrationRequestDto.RegistrationRequestDtoBuilder createRegistrationRequestDto() {
        return RegistrationRequestDto.builder()
                .username("username")
                .firstname("First")
                .lastname("Last Name")
                .email("e@mail.com")
                .dateOfBirth("01/01/1995")
                .address(AddressDtoFactory.createAddressDto().build())
                .account(AccountDto.builder().accountType(AccountType.CHECKING).build());
    }
}