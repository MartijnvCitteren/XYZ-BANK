package com.xyz_bank.onboarding;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.exception.XyzDataAccessException;
import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.Address;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.model.enums.Country;

import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBufferd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TestService {
    private final CustomerRepositoryBufferd customerRepository;

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

        try {
            customerRepository.save(customer);
            Optional<Customer> optionalCustomer = customerRepository.findById(
                    UUID.fromString("011cf093-1ce2-4b25-98d5-ff1e2e608aa8"));
            System.out.println("Optional Customer : " + optionalCustomer.get());
        }catch (BufferedDbException e  ){
            throw new XyzDataAccessException("Error while saving customer");
        }


    }




}
