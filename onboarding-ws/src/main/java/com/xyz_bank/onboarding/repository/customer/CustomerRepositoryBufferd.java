package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface CustomerRepositoryBufferd {
    void save(Customer customer);
}
