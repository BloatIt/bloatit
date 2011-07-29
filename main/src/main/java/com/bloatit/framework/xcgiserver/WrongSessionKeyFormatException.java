package com.bloatit.framework.xcgiserver;

public class WrongSessionKeyFormatException extends Exception {

    private static final long serialVersionUID = 8838570906510336129L;

    public WrongSessionKeyFormatException() {
        super();
    }

    public WrongSessionKeyFormatException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WrongSessionKeyFormatException(final String message) {
        super(message);
    }

    public WrongSessionKeyFormatException(final Throwable cause) {
        super(cause);
    }

}
