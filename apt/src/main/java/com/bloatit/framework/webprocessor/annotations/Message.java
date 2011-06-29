package com.bloatit.framework.webprocessor.annotations;

public final class Message {
    private String message;

    public Message(final String message, final MessageFormater formater) {
        this(formater.format(message));
    }

    public Message(final String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
