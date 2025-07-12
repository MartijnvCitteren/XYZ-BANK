package com.xyz_bank.onboarding.iban;

import com.xyz_bank.onboarding.exception.IbanGenerationException;
import com.xyz_bank.onboarding.exception.XyzDataAccessException;
import com.xyz_bank.onboarding.model.enums.Country;
import com.xyz_bank.onboarding.repository.account.AccountRepositoryBuffered;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DutchIbanGeneratorTest {
    private static final String COUNTRY_CODE_AS_STRING = Country.THE_NETHERLANDS.getCountryCode();
    private static final String XYZB_AS_STRING = "XYZB";
    private static final int MIN_VALUE_CONTROLNUMBER = 2;
    private static final int MAX_VALUE_CONTROLNUMBER = 98;
    private static final int LENGTH_DUTCH_IBAN = 18;

    @Mock
    private AccountRepositoryBuffered accountRepositoryBuffered;

    @InjectMocks
    private DutchIbanGenerator dutchIbanGenerator;


    @Test
    void givenNoIbansCreated_whenGenerateIban_thenReturnCorrectIban() {
        //given
        Long zeroAccountInDB = 0L;
        int valueLastNumber = 1;
        when(accountRepositoryBuffered.count()).thenReturn(zeroAccountInDB);

        //when
        String result = dutchIbanGenerator.generateIban().toString();

        //then
        verify(accountRepositoryBuffered, times(1)).count();
        assertEquals(LENGTH_DUTCH_IBAN,  result.length());
        assertEquals(COUNTRY_CODE_AS_STRING, result.substring(0, 2));
        assertTrue(Integer.parseInt(result.substring(2, 4)) >= MIN_VALUE_CONTROLNUMBER);
        assertTrue(Integer.parseInt(result.substring(2, 4)) <= MAX_VALUE_CONTROLNUMBER);
        assertEquals(XYZB_AS_STRING, result.substring(4, 8));
        assertEquals(valueLastNumber, Integer.parseInt(result.substring(17)));
    }

    @Test
    void given3IbansCreated_whenGenerateIban_thenReturnCorrectIban() {
        //given
        dutchIbanGenerator.generateIban();
        dutchIbanGenerator.generateIban();
        dutchIbanGenerator.generateIban();
        int valueLastNumber = 4;

        //when
        String result = dutchIbanGenerator.generateIban().toString();

        //then
        verify(accountRepositoryBuffered, times(1)).count();
        assertEquals(LENGTH_DUTCH_IBAN,  result.length());
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
        String result = dutchIbanGenerator.generateIban().toString();

        //then
        verify(accountRepositoryBuffered, times(1)).count();
        assertEquals(LENGTH_DUTCH_IBAN,  result.length());
        assertEquals(COUNTRY_CODE_AS_STRING, result.substring(0, 2));
        assertTrue(Integer.parseInt(result.substring(2, 4)) >= MIN_VALUE_CONTROLNUMBER);
        assertTrue(Integer.parseInt(result.substring(2, 4)) <= MAX_VALUE_CONTROLNUMBER);
        assertEquals(XYZB_AS_STRING, result.substring(4, 8));
        assertEquals(valueLastNumbers, Integer.parseInt(result.substring(16)));
    }

    @Test
    void givenException_whenGenerateIban_thenThrowIbanGenerationException() {
        //given
        when(accountRepositoryBuffered.count()).thenThrow(XyzDataAccessException.class);

        //when & then
        assertThrows(IbanGenerationException.class, () -> dutchIbanGenerator.generateIban());
    }

    @Test
    void when50000timesGenerateIban_thenSetContains50000Ibans() {
        //given & when
        int expectedIbanNumbers = 50_000;
        Set<String> set = new HashSet<>();

        for(int i = 1; i <= expectedIbanNumbers; i++){
            set.add(dutchIbanGenerator.generateIban());
        }

        //then
        assertEquals(expectedIbanNumbers, set.size());
    }

}