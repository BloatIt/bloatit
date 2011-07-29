package com.bloatit.data.exceptions;

public class ElementNotFoundException extends Exception {

    private static final long serialVersionUID = -5986694384393178044L;

    public ElementNotFoundException() {
        super();
    }

    public ElementNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ElementNotFoundException(final String message) {
        super(message);
    }

    public ElementNotFoundException(final Throwable cause) {
        super(cause);
    }

}
