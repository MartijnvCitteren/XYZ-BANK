package com.xyz_bank.onboarding.factory;

import com.xyz_bank.onboarding.model.Customer;

import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

public class CustomerFactory {
    public static Customer.CustomerBuilder createCustomer(){
        return Customer.builder()
                .id(UUID.randomUUID())
                .firstName("First")
                .lastName("Last Name")
                .dateOfBirth(LocalDate.of(1995, 01, 01))
                .username("username")
                .password("password")
                .email("e@mail.com");

    }
}
