package com.xyz_bank.onboarding.repository;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import com.xyz_bank.onboarding.factory.CustomerFactory;
import com.xyz_bank.onboarding.model.Customer;
import com.xyz_bank.onboarding.repository.bufferdDb.BufferdDbTask;
import com.xyz_bank.onboarding.repository.bufferdDb.BufferedDbExecutor;
import com.xyz_bank.onboarding.repository.bufferdDb.CallableTask;
import com.xyz_bank.onboarding.repository.bufferdDb.RunnableTask;
import com.xyz_bank.onboarding.repository.customer.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BufferedDbExecutorTest {
    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private BufferedDbExecutor bufferedDbExecutor;

    private static final int MAX_QUEUE_SIZE = 60;
    Method startPrivateExecutor;

    /**
     * Note:
     * Adding reflection to kick-start BufferedDbExecutor. Probably an integration test would be a better
     * way to test this. But with time limitations for now this is imo the best way to validate if functionalities are
     * working. But, feels a bit sketchy.
     */
    @BeforeEach
    void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        startPrivateExecutor = BufferedDbExecutor.class.getDeclaredMethod("startExecutor");
        startPrivateExecutor.setAccessible(true);
        startPrivateExecutor.invoke(bufferedDbExecutor);
    }

    @Test
    void givenQueueReachedMaxDept_whenSubmitRunnable_thenThrowBufferdDbException() {
        assertThrows(BufferedDbException.class, ()-> addNumberOfRunnablesToQueue(MAX_QUEUE_SIZE+15));
    }

    @Test
    void givenQueueIsEmpty_whenSubmitAndExpectResult_thenReturnObject() {
        //given
        UUID uuid = UUID.randomUUID();
        Optional<Customer> optionalCustomer = Optional.of(CustomerFactory.createCustomer().id(uuid).build());
        when(customerRepository.findById(any(UUID.class))).thenReturn(optionalCustomer);

        //when
        Object result = bufferedDbExecutor.submitAndExpectResult(new CallableTask<>(()->customerRepository.findById(uuid)) {});

        //then
        assertEquals(optionalCustomer, result);
    }

    @Test
    void givenQueueReachedMaxDept_whenSubmitAndExpectResult_thenThrowBufferdDbException() {
        assertThrows(BufferedDbException.class, ()-> addNumberOfCallableTasksToQueue(MAX_QUEUE_SIZE + 15));
    }

    @Test
    void givenRunnableTask_whenSubmitAndExpectResult_thenThrowBufferdDbException() {
        assertThrows(BufferedDbException.class, ()-> bufferedDbExecutor.submitAndExpectResult(()-> mock(RunnableTask.class)));
    }






    private void addNumberOfRunnablesToQueue(int numberOfRunnables) {
        for (int i = 0; i < numberOfRunnables; i++) {
            bufferedDbExecutor.submit(mock(BufferdDbTask.class));
        }
    }

    private void addNumberOfCallableTasksToQueue(int numberOfCallableTasks) {
        for (int i = 0; i < numberOfCallableTasks; i++) {
            bufferedDbExecutor.submit(mock(CallableTask.class));
        }
    }


}