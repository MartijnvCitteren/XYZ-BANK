package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.model.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepositoryBuffered {
    void save(Customer customer);

    Optional<Customer> findById(UUID id);

    Map<String, String> getAllUsernamesAndPasswords();

    List<String> getAllUsernames();
}
