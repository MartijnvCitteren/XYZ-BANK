package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.BufferedDbExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerRepositoryImpTest {
    @Mock
    private BufferedDbExecutor bufferedDbExecutor;
    @Mock
    private CustomerRepository customerRepository;
    @InjectMocks
    private CustomerRepositoryImp customerRepositoryImp;

    @Test
    void givenExistingValidUUID_whenFindById_thenReturnCustomer() {
        //given
        UUID id = UUID.randomUUID();
        var customer = Customer.builder().id(id).build();
        when(bufferedDbExecutor.submitWithResult(any(Supplier.class))).thenReturn(Optional.of(customer));

        //when
        Optional<Customer> result = customerRepositoryImp.findById(id);

        //then
        assertEquals(customer, result.get());
    }

}