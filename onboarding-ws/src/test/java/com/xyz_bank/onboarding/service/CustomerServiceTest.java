package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.exception.InvalidRegistrationException;
import com.xyz_bank.onboarding.factory.AccountFactory;
import com.xyz_bank.onboarding.factory.AddressFactory;
import com.xyz_bank.onboarding.factory.RegistrationRequestDtoFactory;
import com.xyz_bank.onboarding.iban.DutchIbanGenerator;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.model.enums.AccountType;
import com.xyz_bank.onboarding.repository.account.AccountRepository;
import com.xyz_bank.onboarding.repository.customer.CustomerRepository;
import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBufferd;
import com.xyz_bank.onboarding.rest.controller.CustomerController;
import com.xyz_bank.onboarding.rest.dto.RegistrationRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private static final String DEFAULT_PW = "default";
    private static final String IBAN = "NL02XYZB1000567890";

    @Mock
    private CustomerRepositoryBufferd customerRepositoryBufferd;
    @Mock
    private AccountService accountService;
    @Mock
    private AddressService addressService;
    @InjectMocks
    private CustomerService customerService;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    void givenValidRegistration_whenRegister_thenReturnResponseWithCorrectValues() throws BufferedDbException {
        //given
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().build();
        when(customerRepositoryBufferd.findAllUsernames()).thenReturn(new ArrayList<>());
        when(accountService.createAccount(request.account())).thenReturn(AccountFactory.createAccount().iban(IBAN).build());
        when(addressService.createAddress(request.address())).thenReturn(AddressFactory.createAddress().build());

        //then
        var result = customerService.register(request);

        //then
        verify(customerRepositoryBufferd, times(1)).findAllUsernames();
        verify(customerRepositoryBufferd, times(1)).save(customerCaptor.capture());
        verify(addressService, times(1)).createAddress(request.address());
        verify(accountService, times(1)).createAccount(request.account());
        assertEquals(request.username(), result.username());
        assertEquals(DEFAULT_PW, result.password());
        assertEquals(IBAN, result.iban());

        //verify correct values are saved
        Customer savedCustomer = customerCaptor.getValue();
        assertEquals(request.username(), savedCustomer.getUsername());
        assertEquals(DEFAULT_PW, savedCustomer.getPassword());
        assertEquals(request.firstname(), savedCustomer.getFirstName());
        assertEquals(request.lastname(), savedCustomer.getLastName());
        assertEquals(request.email(), savedCustomer.getEmail());
        assertEquals(request.dateOfBirth(), savedCustomer.getDateOfBirth());
    }

    @Test
    void givenCustomerUnder18_whenRegister_thenThrowInvalidRegistrationException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateOfBirth = LocalDate.now().minusYears(18).plusDays(1).format(formatter);
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().dateOfBirth(dateOfBirth).build();

        //when & then
        assertThrows(InvalidRegistrationException.class, () -> customerService.register(request));
        verifyNoInteractions(customerRepositoryBufferd);
        verifyNoInteractions(addressService);
        verifyNoInteractions(accountService);
    }

    @Test
    void givenUsernameExistAndNoUsernamesInMemory_whenRegister_thenThrowInvalidRegistrationException()
            throws BufferedDbException {
        //given
        String username = "exist123";
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().username(username).build();
        when(customerRepositoryBufferd.findAllUsernames()).thenReturn(List.of(username));

        //when & then
        assertThrows(InvalidRegistrationException.class, () -> customerService.register(request));
        verify(customerRepositoryBufferd, times(1));
        verifyNoInteractions(addressService);
        verifyNoInteractions(accountService);
    }

    @Test
    void givenUserNameExistsAndUsernamesInMemory_whenRegister_thenThrowInvalidRegistrationException()
            throws BufferedDbException {
        //given - one user gets added
        String username = "exist123";
        var firstRequest = RegistrationRequestDtoFactory.createRegistrationRequestDto().username(username).firstname(
                "Hans").build();
        when(customerRepositoryBufferd.findAllUsernames()).thenReturn(new ArrayList<>());
        when(accountService.createAccount(firstRequest.account())).thenReturn(AccountFactory.createAccount().build());
        when(addressService.createAddress(firstRequest.address())).thenReturn(AddressFactory.createAddress().build());
        customerService.register(firstRequest);

        var realRequest = RegistrationRequestDtoFactory.createRegistrationRequestDto().username(username).firstname(
                "Barry").build();


        //when & then
        assertThrows(InvalidRegistrationException.class, () -> customerService.register(realRequest));
        verify(customerRepositoryBufferd, times(1)).findAllUsernames();
        verify(addressService, times(1)).createAddress(firstRequest.address());
        verify(accountService, times(1)).createAccount(firstRequest.account());
        verify(customerRepositoryBufferd, times(1)).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();
        assertEquals(firstRequest.username(), savedCustomer.getUsername());
    }

}


