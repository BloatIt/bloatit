package com.bloatit.model.data.util;

public class NonOptionalParameterException extends RuntimeException {
    private static final long serialVersionUID = 684365471928810874L;

    public NonOptionalParameterException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonOptionalParameterException(String message) {
        super(message);
    }

    public NonOptionalParameterException(Throwable cause) {
        super(cause);
    }

    public NonOptionalParameterException() {
        super();
    }
}
