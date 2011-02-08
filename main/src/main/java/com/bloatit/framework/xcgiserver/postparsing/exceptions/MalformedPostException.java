package com.bloatit.framework.xcgiserver.postparsing.exceptions;

/**
 * Exception used to represent any malformed post
 */
public class MalformedPostException extends Exception {
    private static final long serialVersionUID = 7045095941160291826L;

    public MalformedPostException() {
        super();
    }

    public MalformedPostException(String message, Throwable cause) {
        super(message, cause);
    }

    public MalformedPostException(String message) {
        super(message);
    }

    public MalformedPostException(Throwable cause) {
        super(cause);
    }
}
