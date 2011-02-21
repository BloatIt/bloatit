package com.bloatit.framework.webserver.annotations;

/**
 * This exception is thrown when a parameter is found, but cannot be converted
 * to the right type.
 */
public class ConversionErrorException extends Exception {
    private static final long serialVersionUID = 1L;

    public ConversionErrorException(final String message) {
        super(message);
    }

    public ConversionErrorException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ConversionErrorException(final Throwable cause) {
        super(cause);
    }
}
