package com.bloatit.common;

public class WrongStateException extends RuntimeException {

    private static final long serialVersionUID = 2471647819390237263L;

    public WrongStateException() {
        super();
    }

    public WrongStateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WrongStateException(final String message) {
        super(message);
    }

    public WrongStateException(final Throwable cause) {
        super(cause);
    }

}
