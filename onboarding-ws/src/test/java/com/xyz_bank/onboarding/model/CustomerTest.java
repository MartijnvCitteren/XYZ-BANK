package com.xyz_bank.onboarding.model;

import com.xyz_bank.onboarding.factory.CustomerFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class CustomerTest {

    @Test
    void givenCustomerWithSameId_whenEquals_returnTrue() {
        //given
        UUID id = UUID.randomUUID();
        var customer1 = CustomerFactory.createCustomer().id(id).build();
        var customer2 = CustomerFactory.createCustomer().id(id).build();

        //when & then
        assertEquals(customer1, customer2);
    }

    @Test
    void givenCustomerWithDifferentId_whenEquals_thenReturnFalse() {
        //given
        var customer1 = CustomerFactory.createCustomer().build();
        var customer2 = CustomerFactory.createCustomer().build();

        //when & then
        assertNotEquals(customer1, customer2);
    }

    @Test
    void givenCustomerWithSameIdButDifferentEmail_whenEquals_thenReturnTrue() {
        //given
        UUID id = UUID.randomUUID();
        var customer1 = CustomerFactory.createCustomer().id(id).build();
        var customer2 = CustomerFactory.createCustomer().id(id).email("different@email.com").build();

        //when & then
        assertEquals(customer1, customer2);
    }
}