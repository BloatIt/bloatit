package com.bloatit.framework.exceptions;

public class MemberNotInGroupException extends Exception {
    private static final long serialVersionUID = -857528512589863370L;

    public MemberNotInGroupException() {
        super();
    }

    public MemberNotInGroupException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberNotInGroupException(String message) {
        super(message);
    }

    public MemberNotInGroupException(Throwable cause) {
        super(cause);
    }
}
