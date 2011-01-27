package com.bloatit.mail;

import com.bloatit.common.FatalErrorException;

public class MailFatalError extends FatalErrorException {
    private static final long serialVersionUID = -1660347313919720091L;

    public MailFatalError(String string, Throwable cause) {
        super(string, cause);
    }

    public MailFatalError(Throwable cause) {
        super(cause);
    }

    public MailFatalError(String message) {
        super(message);
    }

}
