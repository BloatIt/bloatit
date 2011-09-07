package com.bloatit.data.exceptions;

public class UniqueNameExpectedException extends Exception {

    private static final long serialVersionUID = -232838782856080448L;

    public UniqueNameExpectedException() {
    }

    public UniqueNameExpectedException(String message) {
        super(message);
    }

    public UniqueNameExpectedException(Throwable cause) {
        super(cause);
    }

    public UniqueNameExpectedException(String message, Throwable cause) {
        super(message, cause);
    }

}
