package com.xyz_bank.onboarding.repository.customer;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.bufferdDb.BufferedDbExecutor;
import com.xyz_bank.onboarding.repository.bufferdDb.CallableTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Component
@Log4j2
public class CustomerRepositoryImp implements CustomerRepositoryBuffered {
    private final BufferedDbExecutor bufferedDbExecutor;
    private final CustomerRepository customerRepository;

    @Override
    public void save(Customer customer) throws BufferedDbException {
        bufferedDbExecutor.submit(() -> customerRepository.save(customer));
    }

    @Override
    public Optional<Customer> findCustomerByUsername(String username) throws BufferedDbException {
        Object object = bufferedDbExecutor.submitAndExpectResult(
                new CallableTask<>(() -> customerRepository.findCustomerByUsername(username)));
        if (object instanceof Optional<?> optionalCustomer && optionalCustomer.isEmpty()) {
            return Optional.empty();
        } else if (object instanceof Optional<?> optionalCustomer && optionalCustomer.get() instanceof Customer customer) {
            return Optional.of(customer);
        } else {
            throw new IllegalStateException("Wrong object retrieved from database");
        }
    }

    @Override
    public List<String> getAllUsernames() {
        return bufferedDbExecutor.submitAndExpectResult(new CallableTask<>(customerRepository::getAllUsernames));
    }

    @Override
    public Map<String, String> getAllUsernamesAndPasswords() {
        List<Customer> list = bufferedDbExecutor.submitAndExpectResult(new CallableTask<>(customerRepository::findAll));
        Map<String, String> map = new HashMap<>();
        if (list.isEmpty()) {
            return map;
        }
        list.forEach(customer -> map.put(customer.getUsername(), customer.getPassword()));
        return map;
    }


}
