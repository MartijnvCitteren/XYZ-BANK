package com.xyz_bank.onboarding.iban;

import com.xyz_bank.onboarding.model.enums.Country;
import com.xyz_bank.onboarding.repository.account.AccountRepositoryBuffered;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;


class DutchIbanGeneratorTest {
    private static final String COUNTRY_CODE_AS_STRING = Country.THE_NETHERLANDS.getCountryCode();
    private static final String XYZB_AS_STRING = "XYZB";
    private static final int MIN_VALUE_CONTROLNUMBER = 2;
    private static final int MAX_VALUE_CONTROLNUMBER = 98;
    private static final int LENGTH_DUTCH_IBAN = 18;
    private static Long ibansGenrated;

    @Mock
    private AccountRepositoryBuffered accountRepositoryBuffered;

    @InjectMocks
    private DutchIbanGenerator dutchIbanGenerator;

    @BeforeEach
    void setUp() {
        ibansGenrated = 1_000_000_001L; // start value when no Ibans are generated yet
    }

    @Test
    void givenNoIbansCreated_whenGenerateIban_thenReturnCorrectIban() {
        //given
        Long zeroAccountInDB = 0L;
        int valueLastNumber = 1;
        when(accountRepositoryBuffered.count()).thenReturn(zeroAccountInDB);

        //when
        String result = dutchIbanGenerator.generateIban();

        //then
        verify(accountRepositoryBuffered, times(1)).count();
        assertTrue(result.length() <= LENGTH_DUTCH_IBAN);
        assertEquals(COUNTRY_CODE_AS_STRING, result.substring(0, 2));
        assertTrue(Integer.parseInt(result.substring(2, 4)) >= MIN_VALUE_CONTROLNUMBER);
        assertTrue(Integer.parseInt(result.substring(2, 4)) <= MAX_VALUE_CONTROLNUMBER);
        assertEquals(XYZB_AS_STRING, result.substring(4, 8));
        assertEquals(valueLastNumber, Integer.parseInt(result.substring(17)));
    }

    @Test
    void given5IbansCreated_whenGenerateIban_thenReturnCorrectIban() {
        //given
        ibansGenrated = 1_000_000_005L;
        int valueLastNumber = 5;

        //when
        String result = dutchIbanGenerator.generateIban();

        //then
        verifyNoInteractions(accountRepositoryBuffered.count());
        assertTrue(result.length() <= LENGTH_DUTCH_IBAN);
        assertEquals(COUNTRY_CODE_AS_STRING, result.substring(0, 2));
        assertTrue(Integer.parseInt(result.substring(2, 4)) >= MIN_VALUE_CONTROLNUMBER);
        assertTrue(Integer.parseInt(result.substring(2, 4)) <= MAX_VALUE_CONTROLNUMBER);
        assertEquals(XYZB_AS_STRING, result.substring(4, 8));
        assertEquals(valueLastNumber, Integer.parseInt(result.substring(17)));
    }

    @Test
    void givenAppHasBeenOffline_whenGenerateIban_thenContinueCountingInIbanGeneration() {
        //given
        Long accountsInDB = 26L;
        int valueLastNumbers = 27;
        when(accountRepositoryBuffered.count()).thenReturn(accountsInDB);

        //when
        String result = dutchIbanGenerator.generateIban();

        //then
        verify(accountRepositoryBuffered, times(1)).count();
        assertTrue(result.length() <= LENGTH_DUTCH_IBAN);
        assertEquals(COUNTRY_CODE_AS_STRING, result.substring(0, 2));
        assertTrue(Integer.parseInt(result.substring(2, 4)) >= MIN_VALUE_CONTROLNUMBER);
        assertTrue(Integer.parseInt(result.substring(2, 4)) <= MAX_VALUE_CONTROLNUMBER);
        assertEquals(XYZB_AS_STRING, result.substring(4, 8));
        assertEquals(valueLastNumbers, Integer.parseInt(result.substring(16)));
    }

}