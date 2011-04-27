package com.bloatit.framework.xcgiserver.mime;

import java.io.EOFException;

/**
 * An exception used to describe any malformation in a Mime file
 */
public class MalformedMimeException extends Exception {
    private static final long serialVersionUID = -6697390269907534568L;

    protected MalformedMimeException(final String cause) {
        super(cause);
    }

    protected MalformedMimeException(final String cause, final EOFException e) {
        super(cause, e);
    }
}
