package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.model.Customer;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryBuffered {
    void save(Customer customer) throws BufferedDbException;

    Optional<Customer> findById(UUID id) throws BufferedDbException;

    Map<String, String> getAllUsernamesAndPasswords() throws BufferedDbException;
}
