package com.xyz_bank.onboarding.iban;

import com.xyz_bank.onboarding.repository.account.AccountRepositoryBuffered;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.iban4j.CountryCode;
import org.iban4j.Iban;
import org.springframework.stereotype.Component;

@Log4j2
@Component
@RequiredArgsConstructor
public class DutchIbanGenerator {
    private static final String XYZB_IN_DECIMAL_UNICODE = "XYZB";
    private static final CountryCode NL = CountryCode.NL;
    private final Long START_BANKNUMBER = 1_000_000_001L;
    private long numbersGenerated = 0;

    private final AccountRepositoryBuffered accountRepository;

    public String generateIban(){
        if(numbersGenerated == 0){
            numbersGenerated = accountRepository.count();
        }
        Long accountNumber = START_BANKNUMBER + numbersGenerated;
        numbersGenerated++;
       return new Iban.Builder()
                .countryCode(NL)
                .bankCode(XYZB_IN_DECIMAL_UNICODE)
                .accountNumber(accountNumber.toString())
                .build().toString();
    }
}
