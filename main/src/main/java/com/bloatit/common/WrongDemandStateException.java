package com.bloatit.common;

public class WrongDemandStateException extends RuntimeException {

    private static final long serialVersionUID = 2471647819390237263L;

    public WrongDemandStateException() {
        super();
    }

    public WrongDemandStateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public WrongDemandStateException(final String message) {
        super(message);
    }

    public WrongDemandStateException(final Throwable cause) {
        super(cause);
    }

}
