package com.xyz_bank.onboarding.repository.bufferdDb;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class CallableTask<T> implements BufferdDbTask {
    private final Callable<T> callable;
    @Getter
    private final CompletableFuture<T> future = new CompletableFuture<>();

    @Override
    public void execute() throws Exception {
        future.complete(callable.call());
    }

}
