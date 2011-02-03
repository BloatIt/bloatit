package com.bloatit.framework.scgiserver.mime;

public class MalformedMimeException extends Exception {
    private static final long serialVersionUID = -6697390269907534568L;

    public MalformedMimeException(String cause) {
        super(cause);
    }
}
