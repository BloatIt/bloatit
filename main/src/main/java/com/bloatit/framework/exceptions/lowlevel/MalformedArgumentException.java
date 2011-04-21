package com.bloatit.framework.exceptions.lowlevel;

public class MalformedArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = -316403949660231531L;

    public MalformedArgumentException() {
        super();
    }

    public MalformedArgumentException(final String s) {
        super(s);
    }

    public MalformedArgumentException(final Throwable cause) {
        super(cause);
    }

    public MalformedArgumentException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
