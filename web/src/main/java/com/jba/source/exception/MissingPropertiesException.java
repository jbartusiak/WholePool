package com.jba.source.exception;

import java.io.IOException;

public class MissingPropertiesException extends IOException {
    public MissingPropertiesException() {
        super();
    }

    public MissingPropertiesException(String message) {
        super(message);
    }

    public MissingPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

    public MissingPropertiesException(Throwable cause) {
        super(cause);
    }
}
