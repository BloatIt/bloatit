package com.bloatit.framework.exceptions;

public class MemberNotInGroupException extends Exception {
    private static final long serialVersionUID = -857528512589863370L;

    public MemberNotInGroupException() {
        super();
    }

    public MemberNotInGroupException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MemberNotInGroupException(final String message) {
        super(message);
    }

    public MemberNotInGroupException(final Throwable cause) {
        super(cause);
    }
}
