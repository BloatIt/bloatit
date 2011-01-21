package com.bloatit.common;

public class WrongDemandStateException extends RuntimeException {

    private static final long serialVersionUID = 2471647819390237263L;

    public WrongDemandStateException() {
        super();
    }

    public WrongDemandStateException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongDemandStateException(String message) {
        super(message);
    }

    public WrongDemandStateException(Throwable cause) {
        super(cause);
    }

}
