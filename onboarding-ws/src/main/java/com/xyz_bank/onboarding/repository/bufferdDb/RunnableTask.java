package com.xyz_bank.onboarding.repository.bufferdDb;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RunnableTask implements BufferdDbTask {
    private final Runnable task;


    @Override
    public void execute() throws Exception {
        task.run();
    }
}
