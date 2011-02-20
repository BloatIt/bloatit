package com.bloatit.framework.xcgiserver.mime;

public class InvalidMimeEncodingException extends Exception {
    private static final long serialVersionUID = -1100275740713977221L;

    public InvalidMimeEncodingException(final String string) {
        super(string);
    }
}
