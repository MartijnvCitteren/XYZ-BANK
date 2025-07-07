package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.BufferedDbExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Log4j2
public class CustomerRepositoryImp implements CustomerRepositoryBufferd {
    private final BufferedDbExecutor bufferedDbExecutor;
    private final CustomerRepository customerRepository;

    @Override
    public void save(Customer customer) {
        bufferedDbExecutor.submit(() -> customerRepository.save(customer));
    }

    public Optional<Customer> findById(UUID id) {
        Object object = bufferedDbExecutor.submitWithResult(() -> customerRepository.findById(id));
        if (object instanceof Optional<?> optionalCustomer && optionalCustomer.isEmpty()) {
            return Optional.empty();
        }

        if (object instanceof Optional<?> optionalCustomer && optionalCustomer.get() instanceof Customer customer) {
            return Optional.of(customer);
        } else {
            throw new IllegalStateException("Wrong object retrieved from database");
        }

    }
}
