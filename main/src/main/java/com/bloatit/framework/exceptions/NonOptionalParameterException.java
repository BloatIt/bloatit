package com.bloatit.framework.exceptions;

public class NonOptionalParameterException extends RuntimeException {
    private static final long serialVersionUID = 684365471928810874L;

    public NonOptionalParameterException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NonOptionalParameterException(final String message) {
        super(message);
    }

    public NonOptionalParameterException(final Throwable cause) {
        super(cause);
    }

    public NonOptionalParameterException() {
        super();
    }
}
