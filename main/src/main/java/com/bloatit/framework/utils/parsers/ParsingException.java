package com.bloatit.framework.utils.parsers;


/**
 * A class used to represent exceptions that happen during parsing
 */
public class ParsingException extends Exception {
    private static final long serialVersionUID = -799001116243508781L;

    public ParsingException(String cause, Throwable e) {
        super(cause, e);
    }
}
