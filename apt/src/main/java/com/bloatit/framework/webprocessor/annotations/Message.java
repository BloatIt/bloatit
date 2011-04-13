package com.bloatit.framework.webprocessor.annotations;

import java.util.regex.Matcher;

public class Message {
    public enum What {
        UNKNOWN, NOT_FOUND, CONVERSION_ERROR, MIN_ERROR, MAX_ERROR, NO_ERROR, LENGTH_ERROR, OPTIONAL_ERROR, PRECISION_ERROR
    }

    private final String message;
    private final What what;

    public Message(final String message, final What what, final String name, final String value) {
        super();
        if (what == null || message == null) {
            throw new NullPointerException();
        }
        this.message = extractErrorMessage(message, name, value);
        this.what = what;
    }

    public Message(final String message) {
        this.message = message;
        this.what = What.UNKNOWN;
    }

    private String extractErrorMessage(final String aMessage, final String name, final String value) {
        String errorMsg = aMessage.replaceAll("%param", Matcher.quoteReplacement(name));
        if (!value.isEmpty()) {
            errorMsg = errorMsg.replaceAll("%value", Matcher.quoteReplacement(value));
        } else {
            errorMsg = errorMsg.replaceAll("%value", "null");
        }
        return errorMsg;
    }

    public String getMessage() {
        return message;
    }

    public What getWhat() {
        return what;
    }
}
