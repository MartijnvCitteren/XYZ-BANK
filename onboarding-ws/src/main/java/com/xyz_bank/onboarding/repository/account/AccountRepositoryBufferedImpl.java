package com.xyz_bank.onboarding.repository.account;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccountRepositoryBufferedImpl implements AccountRepositoryBuffered {
    private final AccountRepository accountRepository;

    @Override
    public Long count() {
        return accountRepository.count();
    }
}
