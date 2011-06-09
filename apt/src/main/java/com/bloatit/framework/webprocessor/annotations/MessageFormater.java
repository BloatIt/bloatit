package com.bloatit.framework.webprocessor.annotations;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;

public class MessageFormater {

    private Map<String, String> params = new HashMap<String, String>();

    public MessageFormater(final String name, final String value) {
        params.put(name, value);
    }

    public void addParameter(final String name, final String value) {
        params.put(name, value);
    }

    public String format(final String message) {
        String errorMsg = message;
        for (final Entry<String, String> formatter : params.entrySet()) {
            if (!formatter.getValue().isEmpty()) {
                errorMsg = message.replaceAll(formatter.getKey(), Matcher.quoteReplacement(formatter.getValue()));
            } else {
                errorMsg = message.replaceAll(formatter.getKey(), "null");
            }
        }
        return errorMsg;
    }
}
