package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.exception.InvalidRegistrationException;
import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.Address;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.model.enums.Country;
import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBufferd;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerService {
    private static final String DEFAULT_PW = "default";
    private static final List<Country> ELIGIBLE_COUNTRIES = List.of(Country.THE_NETHERLANDS, Country.BELGIUM);
    private final CustomerRepositoryBufferd customerRepositoryBufferd;
    private final AccountService accountService;
    private final AddressService addressService;
    private Set<String> existingUsernames = new HashSet<>();


    public RegistrationResponseDto register(RegistrationRequestDto registration) {
        if (isUnder18Yo(registration.dateOfBirth()) || countryIsNotEligble(registration.address().country())) {
            throw new InvalidRegistrationException("Invalid registration due to age of country of residence");
        }

        if (userNameAlreadyExists(registration.username())) {
            throw new InvalidRegistrationException("Username already exists, please try another username");
        }

        Address address = addressService.createAddress(registration.address());
        Account account = accountService.createAccount(registration.account());
        Customer customer = createCustomer(registration, account, address);
        customerRepositoryBufferd.save(customer);

        return RegistrationResponseDto.builder()
                .username(customer.getUsername())
                .password(DEFAULT_PW)
                .iban(account.getIban())
                .build();
    }

    private LocalDate convertToLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception e) {
            throw new InvalidRegistrationException("Invalid registration due to date format");
        }
    }

    private boolean isUnder18Yo(String dateOfBirth) {
        LocalDate today = LocalDate.now();
        LocalDate birthDate = convertToLocalDate(dateOfBirth);
        return !birthDate.isBefore(today.minusYears(18));
    }

    private boolean countryIsNotEligble(Country country) {
        return !ELIGIBLE_COUNTRIES.contains(country);
    }

    private boolean userNameAlreadyExists(String username) throws BufferedDbException {
        if (existingUsernames.isEmpty()) {
            existingUsernames.addAll(customerRepositoryBufferd.findAllUsernames());
        }

        if (existingUsernames.contains(username)) {
            return true;
        } else {
            existingUsernames.add(username);
            return false;
        }

    }

    private Customer createCustomer(RegistrationRequestDto registration, Account account, Address address) {
        Customer customer = new Customer();
        customer.setEmail(registration.email());
        customer.setUsername(registration.username());
        customer.setPassword(DEFAULT_PW);
        customer.setDateOfBirth(convertToLocalDate(registration.dateOfBirth()));
        customer.setFirstName(registration.firstname());
        customer.setLastName(registration.lastname());
        customer.setAddress(address);
        customer.setAccounts(List.of(account));
        return customer;
    }

}




