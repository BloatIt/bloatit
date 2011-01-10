package com.bloatit.web.utils.url;

import com.bloatit.web.annotations.Message;
import com.bloatit.web.annotations.Message.Level;
import com.bloatit.web.annotations.Message.What;

public final class UrlMessageFactory {

    private final String notFoundMessage;
    private final String malformedMessage;
    private final Level level;

    public UrlMessageFactory(String notFoundMessage, String malformedMessage, Level level) {
        super();
        this.notFoundMessage = notFoundMessage;
        this.malformedMessage = malformedMessage;
        this.level = level;
    }

    public Message createMessage(What what, String name, String value) {
        if (what == What.NO_ERROR) {
            return null;
        }
        String errorMsg = "";
        if (what == What.CONVERSION_ERROR) {
            errorMsg = extractErrorMessage(malformedMessage, name, value);
        } else if (what == What.NOT_FOUND) {
            errorMsg = extractErrorMessage(notFoundMessage, name, value);
        }
        return new Message(level, what, errorMsg);
    }

    private String extractErrorMessage(String message, String name, String value) {
        String errorMsg = message.replaceAll("%param", name);
        if (!value.isEmpty()) {
            errorMsg = message.replaceAll("%value", value);
        } else {
            errorMsg = message.replaceAll("%value", "null");
        }
        return errorMsg;
    }

}
