package com.xyz_bank.onboarding.iban;

import lombok.extern.log4j.Log4j2;
import org.iban4j.CountryCode;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class DutchIbanGenerator {
    private static final String XYZB_IN_DECIMAL_UNICODE = "88899066";
    private static final CountryCode COUNTRY_CODE = CountryCode.NL;

    public String generateIban(){
        return null;
    }
}
