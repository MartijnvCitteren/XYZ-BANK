package com.xyz_bank.onboarding.rest;

import com.xyz_bank.onboarding.exception.IbanGenerationException;
import com.xyz_bank.onboarding.exception.XyzDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({XyzDataAccessException.class})
    public ResponseEntity<?> handleXyzDataAccessException(XyzDataAccessException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler({IbanGenerationException.class})
    public ResponseEntity<?> handleIbanGenerationException(IbanGenerationException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
}
