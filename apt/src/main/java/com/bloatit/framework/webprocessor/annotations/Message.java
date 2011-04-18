package com.bloatit.framework.webprocessor.annotations;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

public class Message {
    public enum What {
        UNKNOWN, NOT_FOUND, CONVERSION_ERROR, MIN_ERROR, MAX_ERROR, NO_ERROR, LENGTH_ERROR, OPTIONAL_ERROR, PRECISION_ERROR
    }

    private final String message;
    private final What what;

    public Message(final String message, final What what, Map<String,String> formatMap) {
        super();
        if (what == null || message == null) {
            throw new NullPointerException();
        }
        this.message = extractErrorMessage(message, formatMap);
        this.what = what;
    }

    public Message(final String message) {
        this.message = message;
        this.what = What.UNKNOWN;
    }

    private String extractErrorMessage(final String aMessage, Map<String,String> formatMap) {
        String errorMsg = aMessage;
        for(Entry<String,String> formatter: formatMap.entrySet()) {
            if(!formatter.getValue().isEmpty()) {
                errorMsg = errorMsg.replaceAll(formatter.getKey(), Matcher.quoteReplacement(formatter.getValue()));
            } else {
                errorMsg = errorMsg.replaceAll(formatter.getKey(), "null");
            }
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
