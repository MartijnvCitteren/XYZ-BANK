package com.xyz_bank.onboarding.repository;

import com.xyz_bank.onboarding.exception.BufferedDbException;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;



@Log4j2
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Component
public class BufferedDbExecutor {
    private static final int HALF_SECOND = 500;
    private static final int MAX_QUEUE_SIZE = 120; //creates max delay of 1 minute
    private final BlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(MAX_QUEUE_SIZE);

    @PostConstruct
    private void startExecutor() {
        Thread executor = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Runnable task = queue.take();
                    task.run();
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


    public void submit(Runnable task) {
        if (!queue.offer(task)) {
            log.error("Queue for bufferd database actions is full");
            throw new BufferedDbException("Queue is full");
        }
    }

    public <T> Object submitWithResult(Supplier<T> task) {
        CompletableFuture<T> future = new CompletableFuture<>();
        if (!queue.offer(() -> {
            try {
                T result = task.get();
                future.complete(result);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        })) {
            log.error("Queue for bufferd database actions is full");
            throw new BufferedDbException("Queue is full");
        }
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            log.error(e.getMessage());
            throw new BufferedDbException(("Getting completable future failed : " + e.getMessage()));
        }

    }


}
