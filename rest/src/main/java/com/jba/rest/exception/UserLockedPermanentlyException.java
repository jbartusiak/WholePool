package com.jba.rest.exception;

public class UserLockedPermanentlyException extends RuntimeException {
    public UserLockedPermanentlyException() {
        super();
    }

    public UserLockedPermanentlyException(String message) {
        super(message);
    }

    public UserLockedPermanentlyException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLockedPermanentlyException(Throwable cause) {
        super(cause);
    }

    protected UserLockedPermanentlyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

