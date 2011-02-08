package com.bloatit.framework.xcgiserver.mime;

import java.io.EOFException;

/**
 * An exception used to describe any malformation in a Mime file
 */
public class MalformedMimeException extends Exception {
    private static final long serialVersionUID = -6697390269907534568L;

    public MalformedMimeException(String cause) {
        super(cause);
    }

    public MalformedMimeException(String cause, EOFException e) {
        super(cause, e);
    }
}
