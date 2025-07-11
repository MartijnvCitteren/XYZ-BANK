package com.xyz_bank.onboarding.iban;

import com.xyz_bank.onboarding.exception.IbanGenerationException;
import com.xyz_bank.onboarding.repository.account.AccountRepositoryBuffered;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Log4j2
@Component
@RequiredArgsConstructor
public class DutchIbanGenerator {
    private static final String XYZB = "XYZB";
    private static final CountryCode NL = CountryCode.NL;
    private final long startBanknumber = 1_000_000_001L;
    private final AccountRepositoryBuffered accountRepository;
    private AtomicLong numbersGenerated = new AtomicLong(0);

    public String generateIban() throws IbanGenerationException {
        try {
            if (numbersGenerated.intValue() == 0) {
                numbersGenerated.set(accountRepository.count());
            }
            Long accountNumber = startBanknumber + numbersGenerated.intValue();
            numbersGenerated.incrementAndGet();
            return new Iban.Builder()
                    .countryCode(NL)
                    .bankCode(XYZB)
                    .accountNumber(accountNumber.toString())
                    .build().toString();
        }
        catch (Exception e) {
            log.error("Error creating Dutch Iban: " + e);
            throw new IbanGenerationException(e.getMessage());
        }

    }
}
