package com.bloatit.data.exceptions;

public class UniqueNameExpectedException extends Exception {

    private static final long serialVersionUID = -232838782856080448L;

    public UniqueNameExpectedException() {
    }

    public UniqueNameExpectedException(final String message) {
        super(message);
    }

    public UniqueNameExpectedException(final Throwable cause) {
        super(cause);
    }

    public UniqueNameExpectedException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
