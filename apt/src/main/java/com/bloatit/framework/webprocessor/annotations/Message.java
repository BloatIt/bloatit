package com.bloatit.framework.webprocessor.annotations;

public class Message {
    private String message;

    public Message(final String message, final MessageFormater formater) {
        super();
        this.message = formater.format(message);
    }

    public Message(final String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
