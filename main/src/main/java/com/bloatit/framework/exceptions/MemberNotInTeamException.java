package com.bloatit.framework.exceptions;

public class MemberNotInTeamException extends Exception {
    private static final long serialVersionUID = -857528512589863370L;

    public MemberNotInTeamException() {
        super();
    }

    public MemberNotInTeamException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public MemberNotInTeamException(final String message) {
        super(message);
    }

    public MemberNotInTeamException(final Throwable cause) {
        super(cause);
    }
}
