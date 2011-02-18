package com.bloatit.rest;

public class NotAvailableRestMethodException extends Exception{
    private static final long serialVersionUID = 1789724106068640033L;

    public NotAvailableRestMethodException() {
        super();
    }

    public NotAvailableRestMethodException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAvailableRestMethodException(String message) {
        super(message);
    }

    public NotAvailableRestMethodException(Throwable cause) {
        super(cause);
    }
}
