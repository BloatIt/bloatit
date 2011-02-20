package com.bloatit.rest;

public class NotAvailableRestMethodException extends Exception {
    private static final long serialVersionUID = 1789724106068640033L;

    public NotAvailableRestMethodException() {
        super();
    }

    public NotAvailableRestMethodException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NotAvailableRestMethodException(final String message) {
        super(message);
    }

    public NotAvailableRestMethodException(final Throwable cause) {
        super(cause);
    }
}
