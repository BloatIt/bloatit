package com.bloatit.rest;

public class InvalidRightException extends Exception {
    private static final long serialVersionUID = 1789724106068640033L;

    public InvalidRightException() {
        super();
    }

    public InvalidRightException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidRightException(final String message) {
        super(message);
    }

    public InvalidRightException(final Throwable cause) {
        super(cause);
    }
}
