package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.bufferdDb.BufferedDbExecutor;
import com.xyz_bank.onboarding.repository.bufferdDb.CallableTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Log4j2
public class CustomerRepositoryImp implements CustomerRepositoryBufferd {
    private final BufferedDbExecutor bufferedDbExecutor;
    private final CustomerRepository customerRepository;

    @Override
    public void save(Customer customer) throws BufferedDbException {
        bufferedDbExecutor.submit(() -> customerRepository.save(customer));
    }

    @Override
    public Optional<Customer> findById(UUID id) throws BufferedDbException {
        Object object = bufferedDbExecutor.submitAndExpectResult(new CallableTask<>(() -> customerRepository.findById(id)));
        if (object instanceof Optional<?> optionalCustomer && optionalCustomer.isEmpty()) {
            return Optional.empty();
        } else if (object instanceof Optional<?> optionalCustomer && optionalCustomer.get() instanceof Customer customer) {
            return Optional.of(customer);
        } else {
            throw new IllegalStateException("Wrong object retrieved from database");
        }
    }

    @Override
    public List<String> findAllUsernames() throws BufferedDbException {
        return bufferedDbExecutor.submitAndExpectResult(new CallableTask<>(customerRepository::findAllUsernames));


    }


}
