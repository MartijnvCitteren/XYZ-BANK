package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    @Query(value = "SELECT username FROM Customer")
    List<String> getAllUsernames();

    Optional<Customer> findCustomerByUsername(String username);


}
