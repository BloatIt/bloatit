package com.bloatit.framework.utils.parsers;

/**
 * A class used to represent exceptions that happen during parsing
 */
public class ParsingException extends Exception {
    private static final long serialVersionUID = -799001116243508781L;

    protected ParsingException(final String cause, final Throwable e) {
        super(cause, e);
    }
}
