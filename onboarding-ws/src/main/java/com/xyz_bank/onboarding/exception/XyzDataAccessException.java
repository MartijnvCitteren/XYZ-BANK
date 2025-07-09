package com.xyz_bank.onboarding.exception;

import org.springframework.dao.DataAccessException;

public class XyzDataAccessException extends DataAccessException {
    public XyzDataAccessException(String msg) {
        super(msg);
    }

    public XyzDataAccessException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
