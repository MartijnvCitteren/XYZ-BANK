package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.factory.AccountFactory;
import com.xyz_bank.onboarding.factory.CustomerFactory;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.bufferdDb.BufferdDbTask;
import com.xyz_bank.onboarding.repository.bufferdDb.BufferedDbExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryImpTest {
    @Mock
    private BufferedDbExecutor bufferedDbExecutor;

    @InjectMocks
    private CustomerRepositoryImp customerRepositoryImp;

    @Test
    void givenCustomer_whenSave_thenUseBufferdDbExecutor() throws BufferedDbException {
        //given & when
        customerRepositoryImp.save(any(Customer.class));

        //then
        verify(bufferedDbExecutor, times(1)).submit(any(BufferdDbTask.class));
    }

    @Test
    void givenExistingValidUUID_whenFindByUserName_thenReturnCustomer() throws BufferedDbException {
        //given
        String username = "username";
        var customer = CustomerFactory.createCustomer().username(username).build();
        when(bufferedDbExecutor.submitAndExpectResult(any(BufferdDbTask.class))).thenReturn(Optional.of(customer));

        //when
        Optional<Customer> result = customerRepositoryImp.findCustomerByUsername(username);

        //then
        verify(bufferedDbExecutor, times(1)).submitAndExpectResult(any(BufferdDbTask.class));
        assertEquals(customer, result.get());
    }

    @Test
    void givenNonExistingUUID_whenFindById_thenReturnEmptyOptional() throws BufferedDbException {
        //given
        when(bufferedDbExecutor.submitAndExpectResult(any(BufferdDbTask.class))).thenReturn(Optional.empty());

        //when
        Optional<Customer> result = customerRepositoryImp.findCustomerByUsername(anyString());

        //then
        verify(bufferedDbExecutor, times(1)).submitAndExpectResult(any(BufferdDbTask.class));
        assertTrue(result.isEmpty());
    }

    @Test
    void givenExistingUUID_whenFindByIdAndWrongEntiyFound_thenIllegalStateException() throws BufferedDbException {
        //given
        var unexpectedAccount = AccountFactory.createAccount().build();
        when(bufferedDbExecutor.submitAndExpectResult(any(BufferdDbTask.class))).thenReturn(Optional.of(unexpectedAccount));

        //when & then
        assertThrows(IllegalStateException.class, () -> customerRepositoryImp.findCustomerByUsername(anyString()));
        verify(bufferedDbExecutor, times(1)).submitAndExpectResult(any(BufferdDbTask.class));
    }

    @Test
    void givenExistingUUID_whenFindByIdWouldReturnCustomerDirectly_thenIllegalStateException()
            throws BufferedDbException {
        //given
        var customer = CustomerFactory.createCustomer().id(UUID.randomUUID()).build();
        when(bufferedDbExecutor.submitAndExpectResult(any(BufferdDbTask.class))).thenReturn(customer);

        //when & then
        assertThrows(IllegalStateException.class, () -> customerRepositoryImp.findCustomerByUsername(anyString()));
    }

}