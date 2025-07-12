package com.xyz_bank.onboarding.service;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.exception.InvalidRegistrationException;
import com.xyz_bank.onboarding.factory.AccountFactory;
import com.xyz_bank.onboarding.factory.AddressFactory;
import com.xyz_bank.onboarding.factory.RegistrationRequestDtoFactory;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.customer.CustomerRepositoryBuffered;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRegistrationServiceTest {
    private static final String IBAN = "NL02XYZB1000567890";

    @Mock
    private CustomerRepositoryBuffered customerRepositoryBuffered;

    @Mock
    private AccountService accountService;

    @Mock
    private AddressService addressService;

    @InjectMocks
    private CustomerRegistrationService customerRegistrationService;

    @Captor
    private ArgumentCaptor<Customer> customerCaptor;

    @Test
    void givenValidRegistration_whenRegister_thenReturnResponseWithCorrectValues() throws BufferedDbException {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().build();
        when(customerRepositoryBuffered.getAllUsernames()).thenReturn(new ArrayList<>());
        when(accountService.createAccount(request.account())).thenReturn(
                AccountFactory.createAccount().iban(IBAN).build());
        when(addressService.createAddress(request.address())).thenReturn(AddressFactory.createAddress().build());

        //then
        var result = customerRegistrationService.register(request);

        //then
        verify(customerRepositoryBuffered, times(1)).getAllUsernames();
        verify(customerRepositoryBuffered, times(1)).save(customerCaptor.capture());
        verify(addressService, times(1)).createAddress(request.address());
        verify(accountService, times(1)).createAccount(request.account());
        assertEquals(request.username(), result.username());
        assertNotNull(result.password());
        assertEquals(IBAN, result.iban());

        //verify correct values are saved
        Customer savedCustomer = customerCaptor.getValue();
        assertEquals(request.username(), savedCustomer.getUsername());
        assertNotNull(savedCustomer.getPassword());
        assertEquals(request.firstname(), savedCustomer.getFirstName());
        assertEquals(request.lastname(), savedCustomer.getLastName());
        assertEquals(request.email(), savedCustomer.getEmail());
        assertEquals(request.dateOfBirth(), savedCustomer.getDateOfBirth().format(formatter));
    }

    @Test
    void givenCustomerUnder18_whenRegister_thenThrowInvalidRegistrationException() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateOfBirth = LocalDate.now().minusYears(18).plusDays(1).format(formatter);
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().dateOfBirth(dateOfBirth).build();

        //when & then
        assertThrows(InvalidRegistrationException.class, () -> customerRegistrationService.register(request));
        verifyNoInteractions(customerRepositoryBuffered);
        verifyNoInteractions(addressService);
        verifyNoInteractions(accountService);
    }

    @Test
    void givenCustomerBecame18YoToday_whenRegister_thenReturnResponse() {
        //given
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dateOfBirth = LocalDate.now().minusYears(18).format(formatter);
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().dateOfBirth(dateOfBirth).build();
        when(customerRepositoryBuffered.getAllUsernames()).thenReturn(new ArrayList<>());
        when(accountService.createAccount(request.account())).thenReturn(
                AccountFactory.createAccount().iban(IBAN).build());
        when(addressService.createAddress(request.address())).thenReturn(AddressFactory.createAddress().build());

        //when
        var result = customerRegistrationService.register(request);

        //then
        assertNotNull(result);
    }

    @Test
    void givenUsernameExistAndNoUsernamesInMemory_whenRegister_thenThrowInvalidRegistrationException()
            throws BufferedDbException {
        //given
        String username = "exist123";
        var request = RegistrationRequestDtoFactory.createRegistrationRequestDto().username(username).build();
        when(customerRepositoryBuffered.getAllUsernames()).thenReturn(List.of(username));

        //when & then
        assertThrows(InvalidRegistrationException.class, () -> customerRegistrationService.register(request));
        verify(customerRepositoryBuffered, times(1)).getAllUsernames();
        verifyNoInteractions(addressService);
        verifyNoInteractions(accountService);
    }

    @Test
    void givenUserNameExistsAndUsernamesInMemory_whenRegister_thenThrowInvalidRegistrationException()
            throws BufferedDbException {
        //given - one user gets added
        String username = "exist123";
        var firstRequest = RegistrationRequestDtoFactory.createRegistrationRequestDto()
                .username(username)
                .firstname("Hans")
                .build();
        when(customerRepositoryBuffered.getAllUsernames()).thenReturn(new ArrayList<>());
        when(accountService.createAccount(firstRequest.account())).thenReturn(AccountFactory.createAccount().build());
        when(addressService.createAddress(firstRequest.address())).thenReturn(AddressFactory.createAddress().build());
        customerRegistrationService.register(firstRequest);

        var realRequest = RegistrationRequestDtoFactory.createRegistrationRequestDto()
                .username(username)
                .firstname("Barry")
                .build();


        //when & then
        assertThrows(InvalidRegistrationException.class, () -> customerRegistrationService.register(realRequest));
        verify(customerRepositoryBuffered, times(1)).getAllUsernames();
        verify(addressService, times(1)).createAddress(firstRequest.address());
        verify(accountService, times(1)).createAccount(firstRequest.account());
        verify(customerRepositoryBuffered, times(1)).save(customerCaptor.capture());
        Customer savedCustomer = customerCaptor.getValue();
        assertEquals(firstRequest.username(), savedCustomer.getUsername());
    }

}


