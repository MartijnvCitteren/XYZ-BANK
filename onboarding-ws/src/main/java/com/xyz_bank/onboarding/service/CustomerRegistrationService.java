package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.exception.InvalidRegistrationException;
import com.xyz_bank.onboarding.model.Account;
import com.xyz_bank.onboarding.model.Address;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.model.enums.Country;
import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBuffered;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import com.xyz_bank.onboarding.rest.dto.RegistrationResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.text.RandomStringGenerator;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Log4j2
public class CustomerRegistrationService {
    private static final List<Country> ELIGIBLE_COUNTRIES = List.of(Country.THE_NETHERLANDS, Country.BELGIUM);
    private final CustomerRepositoryBuffered customerRepositoryBuffered;
    private final AccountService accountService;
    private final AddressService addressService;
    private Set<String> existingUsernames = Collections.synchronizedSet(new HashSet<>());


    public RegistrationResponseDto register(RegistrationRequestDto registration) {
        if (isUnder18Yo(registration.dateOfBirth())) {
            throw new InvalidRegistrationException("Invalid registration - Customer has to be at least 18 years old");

        } else if (countryIsNotEligble(registration.address().country())) {
            throw new InvalidRegistrationException("Invalid registration - country of residence is not eligible");

        } else if (userNameAlreadyExists(registration.username())) {
            throw new InvalidRegistrationException("Username already exists, please try another username");
        }

        Address address = addressService.createAddress(registration.address());
        Account account = accountService.createAccount(registration.account());
        Customer customer = createCustomer(registration, account, address);
        customerRepositoryBuffered.save(customer);

        return RegistrationResponseDto.builder()
                .username(customer.getUsername())
                .password(customer.getPassword())
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
        return birthDate.isAfter(today.minusYears(18));
    }

    private boolean countryIsNotEligble(Country country) {
        return !ELIGIBLE_COUNTRIES.contains(country);
    }

    private boolean userNameAlreadyExists(String username) throws BufferedDbException {
        if (existingUsernames.isEmpty()) {
            existingUsernames.addAll(customerRepositoryBuffered.getAllUsernames());
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
        customer.setPassword(generatePassword());
        customer.setDateOfBirth(convertToLocalDate(registration.dateOfBirth()));
        customer.setFirstName(registration.firstname());
        customer.setLastName(registration.lastname());
        customer.setAddress(address);
        customer.setAccounts(List.of(account));
        return customer;
    }

    private String generatePassword() {
        RandomStringGenerator pwdGenerator = new RandomStringGenerator.Builder().withinRange(65, 122).build();
        return pwdGenerator.generate(8);
    }
}




