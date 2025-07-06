package com.xyz_bank.onboarding;

import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.Address;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Country;
import com.xyz_bank.onboarding.repository.CustomerRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TestService {
    private final CustomerRepository customerRepository;

    public void createCustomer() {
        //test service to validate if an entity is saved properly
        Address address = Address.builder()
                .city("Leiden")
                .country(Country.THE_NETHERLANDS)
                .houseNumber("27A")
                .zipCode("2342CT")
                .build();

        Account account = Account.builder()
                .type(AccountType.CHECKING)
                .iban("NL89XYZB0000102020")
                .balance(BigDecimal.valueOf(100))
                .build();

        Customer customer = Customer.builder()
                .firstName("Martijn")
                .address(address)
                .accounts(List.of(account))
                .build();

        customerRepository.save(customer);

    }


}
