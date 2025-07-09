package com.xyz_bank.onboarding.repository.bufferdDb;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


/**
 * Notes:
 * Class to make sure that the DB doesn't get more than 2 calls a second this class buffers al DB requests.
 * To be honest, This felt a bit above my skill / experience level. General idea is from me but during implementation
 * AI support was needed. I think I understand all I implemented. But this would definitly be a class I would double
 * check with senior devs.
 * <p>
 * Also hard to test, quit some overhead if the project grows. Not really a fan and definitlu some cons.
 */


@Log4j2
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Component
public class BufferedDbExecutor {
    private static final int HALF_SECOND = 500;
    private static final int MAX_QUEUE_SIZE = 30; //creates max delay of 15 seconds
    private final BlockingQueue<BufferdDbTask> queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);


    public void submit(BufferdDbTask task) throws BufferedDbException {
        if (!queue.offer(task)) {
            log.error("Queue for bufferd database actions is full");
            throw new BufferedDbException("Queue is full");
        }
    }

    public <T> T submitAndExpectResult(BufferdDbTask task) throws BufferedDbException {
        CallableTask<T> callable = castToCallableTask(task);
        if (queue.offer(callable)) {
            return callable.getFuture().join();
        } else {
            throw new BufferedDbException("Queue is full");
        }
    }

    private <T> CallableTask<T> castToCallableTask(BufferdDbTask task) throws BufferedDbException {
        CallableTask<T> submitTask;
        if (task instanceof CallableTask<?> callableTask) {
            submitTask = (CallableTask<T>) callableTask;
        } else {
            throw new BufferedDbException("The task you submitted is not a callable task and can't return a result.");
        }
        return submitTask;
    }

    @PostConstruct
    private void startExecutor() {
        Thread executor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    BufferdDbTask task = queue.take();
                    task.execute();
                    Thread.sleep(HALF_SECOND);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Start executor method is interrupted");
                    break;
                } catch (Exception e) {
                    log.error("Error while executing task with this message : " + e.getMessage());
                }
            }
        });
        executor.setDaemon(true);
        executor.start();
    }


}
