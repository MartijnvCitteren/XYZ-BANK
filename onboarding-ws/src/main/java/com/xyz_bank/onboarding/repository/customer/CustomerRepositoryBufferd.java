package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.model.Customer;

import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryBufferd {
    void save(Customer customer);
    Optional<Customer> findById(UUID id);
}
