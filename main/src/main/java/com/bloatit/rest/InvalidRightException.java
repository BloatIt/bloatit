package com.bloatit.rest;

public class InvalidRightException extends Exception{
    private static final long serialVersionUID = 1789724106068640033L;

    public InvalidRightException() {
        super();
    }

    public InvalidRightException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidRightException(String message) {
        super(message);
    }

    public InvalidRightException(Throwable cause) {
        super(cause);
    }
}
